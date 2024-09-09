package core

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect
import combo.*
import config.RelicComboModConfig
import ui.ComboPowerTip
import utils.isInCombat
import utils.toLog
import kotlin.math.max

abstract class AbstractRelicCombo(
    val id: String, val combo: HashSet<String> = hashSetOf(), var numberToActive: Int = 3
) {
    var title: String = ""
    var desc: String = ""
    private var tip: ComboPowerTip = ComboPowerTip(title, desc, id)
    private val flavorTexts: ArrayList<String> = arrayListOf()
    private val relicStrings = CardCrawlGame.languagePack?.getRelicStrings(id)

    init {
        relicStrings?.apply {
            title = relicStrings.NAME
            desc = relicStrings.DESCRIPTIONS.joinToString(" NL ") { it.replace(" !N! ", numberToActive.toString()) }
            if (relicStrings.FLAVOR == null || relicStrings.FLAVOR == "") {
                flavorTexts.add(title)
            } else {
                flavorTexts.addAll(relicStrings.FLAVOR.split(" NL "))
            }
            tip = ComboPowerTip(title, desc, id)
        }
        combo.removeIf { !RelicLibrary.isARelic(it) }
        updateTip()
    }


    abstract fun onActive()

    open fun isActive(combo: HashSet<String>): Boolean {
        return combo.size >= numberToActive
    }


    open fun getTip(idToHighlight: HashSet<String> = hashSetOf()): PowerTip {
        updateTip(idToHighlight)
        return tip
    }


    open fun flash() {
        val msg = flavorTexts.randomOrNull() ?: title
        val textAboveCreatureEffect = TextAboveCreatureEffect(
            AbstractDungeon.player.hb.cX - AbstractDungeon.player.animX,
            AbstractDungeon.player.hb.cY + AbstractDungeon.player.hb.height / 2.0f,
            msg,
            Color.GOLD.cpy()
        )
        AbstractDungeon.topLevelEffectsQueue.add(
            textAboveCreatureEffect
        )
    }

    private fun updateTip(idToHighlight: HashSet<String> = hashSetOf()) {
        desc =
            relicStrings?.DESCRIPTIONS?.joinToString(" NL ") { it.replace(" !N! ", numberToActive.toString()) } ?: desc
        val s = combo.joinToString(separator = " NL ") {
            var color = "#b"
            if (it in idToHighlight) {
                color = "#g"
            }
            "· ${
                CardCrawlGame.languagePack.getRelicStrings(it).NAME.split(" ")
                    .joinToString(prefix = color, separator = " $color")
            }"
        }
        tip = ComboPowerTip(title, "$s NL $desc", id)
    }

    fun getCurrentComboSize(): Int {
        return currentComboSet[this]?.size ?: 0
    }

    fun getExtraCollectCount(): Int {
        return max(0, getCurrentComboSize() - numberToActive)
    }

    companion object {

        // registered combo
        val registeredComboSet = hashSetOf<AbstractRelicCombo>()

        fun registerComboSet(comboSet: ArrayList<AbstractRelicCombo>) {
            registeredComboSet.addAll(comboSet)
        }

        // enabled combo
        val enabledRelicComboSets = hashSetOf<AbstractRelicCombo>()

        // relic id that included in enabled combo
        private val allId = hashSetOf<String>()

        // update the enabled combo , call after setting changed
        fun updateEnabledRelicComboSets() {
            enabledRelicComboSets.clear()
            val filter: ArrayList<AbstractRelicCombo> = arrayListOf()

            registeredComboSet.forEach {
                if (RelicComboModConfig.spireModConfig?.getBool(
                        RelicComboModConfig.getPropertyKey(
                            it,
                            RelicComboModConfig.Companion.ComboConfig.EnableCombo
                        )
                    ) == true
                ) {
                    filter.add(it.javaClass.newInstance())
                }
                it.combo.forEach {
                    RelicLibrary.getRelic(it).tips.removeIf { tip -> tip is ComboPowerTip }
                }
            }
            filter.forEach {
                it.numberToActive = RelicComboModConfig.spireModConfig?.getInt(
                    RelicComboModConfig.getPropertyKey(
                        it,
                        RelicComboModConfig.Companion.ComboConfig.NumberToActive
                    )
                ) ?: it.numberToActive
            }
            enabledRelicComboSets.addAll(filter)
            enabledRelicComboSets.forEach { s ->
                s.combo.forEach { id ->
                    val relic = RelicLibrary.getRelic(id)
                    relic?.tips?.add(s.getTip(hashSetOf(id)))
                }
                allId.addAll(s.combo)
            }
        }


        // player current combo set
        val currentComboSet: HashMap<AbstractRelicCombo, HashSet<String>> = hashMapOf()

        // default combo sets
        fun getAllDefaultComboSet(): ArrayList<AbstractRelicCombo> {
            return arrayListOf(
                Alchemy(),
                AncientCreation(),
                Awakened(),
                BringInTheBucks(),
                ChallengeMe(),
                CounterAttack(),
                DeepMeditation(),
                Dispel(),
                Eternal(),
                FourElement(),
                GetSomeSleep(),
                Innocence(),
                KnowledgeIsPower(),
                KungFu(),
                LessIsMore(),
                Library(),
                Mechanism(),
                MightFortune(),
                Ninja(),
                Sacrifice(),
                Plain(),
                Repair(),
                SetSail(),
                Stargazing(),
                StayHealthy(),
                TheThreeOfUs(),
                ToxinPurification(),
                VIP(),
//            StayUp(),
                Yummy()
            )
        }

        // update current combo set , call after get/remove any relic
        fun updateCombo() {
            if (AbstractDungeon.player != null) {
                val relics = AbstractDungeon.player.relics.map { it.relicId }
                "update combo set start".toLog()
//              clean all triggerable effect for all combo
                PatchEffect.cleanAllComboEffect()
                PatchEffect.listOfAll.sumOf { it.size }.toLog("list size")

//              remove non active combo
                if (!isInCombat()) {
                    currentComboSet.clear()
                } else {
                    val filter = currentComboSet.filter { it.key.isActive(it.value) }
                    currentComboSet.clear()
                    currentComboSet.putAll(filter)
                    currentComboSet.forEach { (_, v) ->
                        v.removeIf { it !in relics }
                    }
                }


                //update set
                relics.forEach { r ->
                    if (r in allId) {
                        enabledRelicComboSets.forEach { s ->
                            if (r in s.combo) {
                                val find = currentComboSet.entries.find { it.key.id == s.id }
                                val k =
                                    find?.key ?: s.javaClass.newInstance()
                                        .apply { numberToActive = s.numberToActive }
                                val v = find?.value ?: hashSetOf()
                                v.add(r)
                                currentComboSet[k] = v
                            }
                        }
                    }
                }
                currentComboSet.size.toLog("combo set size")
                // subscribe triggerable effect for all active combo (By trigger onActive)
                currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        "active combo ${k.title}".toLog()
                        k.onActive()
                    }
                }
                PatchEffect.sortByPriority()
                // update desc
                AbstractDungeon.player.relics.forEach { r ->
                    r.tips.removeIf { it is ComboPowerTip }
                    currentComboSet.forEach { (k, v) ->
                        if (r.relicId in k.combo) {
                            r.tips.add(k.getTip(v))
                        }
                    }
                }
            }
        }


    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AbstractRelicCombo
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

