package core

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect
import combo.*
import config.RelicComboModConfig
import patch.RelicComboFieldPatch
import ui.ComboPowerTip
import utils.isInCombat
import utils.toLog
import kotlin.math.max

abstract class AbstractRelicCombo(
    val id: String,
    //TODO make this configurable
    val combo: HashSet<String> = hashSetOf(),
    numberToActiveCombo: Int = 3, enableCombo: Boolean = true,
    canRelicRepeat: Boolean = true
) {
    val configurablePropertySet: HashMap<String, ConfigurableProperty> = hashMapOf()
    var title: String = ""
    var desc: String = ""
    private var tip: ComboPowerTip = ComboPowerTip(title, desc, id)
    private val flavorTexts: ArrayList<String> = arrayListOf()
    private val relicStrings = CardCrawlGame.languagePack?.getRelicStrings(id)

    class ConfigurableProperty(val key: String, val default: Any, val name: String, val type: ConfigurableType)

    var numberToActive: Int = setConfigurableProperty(NumberToActive, numberToActiveCombo, ConfigurableType.Int).toInt()
    val enableCombo: Boolean = setConfigurableProperty(EnableCombo, enableCombo, ConfigurableType.Bool).toBoolean()
    val canRelicRepeat: Boolean =
        setConfigurableProperty(CanRelicRepeat, canRelicRepeat, ConfigurableType.Bool).toBoolean()

    /**
     * call this function during init or manually add Property to configurablePropertySet
     * ,otherwise it won't generate right config ui
     */
    fun setConfigurableProperty(
        name: String, default: Any, type: ConfigurableType
    ): PropertyString {
        val k = getConfigurablePropertyKey(name)
        val d = default.toString()
        var r: String = d
        configurablePropertySet[k] = ConfigurableProperty(key = k, default = d, name = name, type = type)
        RelicComboModConfig.spireModConfig?.apply {
            if (has(k)) {
                r = getString(k)
            } else {
                setString(k, d)
            }
        }
        return PropertyString(r, d)
    }

    // (Cast Exception may happen when change an old property's type or have a wrong default value)
    class PropertyString(private val value: String, private val default: Any) {
        fun toInt(): Int {
            val i: Int = try {
                value.toInt()
            } catch (e: NumberFormatException) {
                default.toString().toInt()
            }
            return i
        }

        fun toBoolean(): Boolean {
            val b: Boolean = try {
                toBooleanStrict(value)
            } catch (e: IllegalArgumentException) {
                toBooleanStrict(default.toString())
            }
            return b
        }

        private fun toBooleanStrict(s: String): Boolean = when (s) {
            "true" -> true
            "false" -> false
            else -> throw IllegalArgumentException("The string doesn't represent a boolean value: $s")
        }
    }


    /**
     *  will throw an exception when get a null property
     */
    private fun getConfigurableProperty(name: String): PropertyString {
        val k = getConfigurablePropertyKey(name)
        val d = configurablePropertySet[k]?.default?.toString()
        var r: String? = d
        if (r == null || d == null) {
            throw RuntimeException("get a property before set")
        }
        RelicComboModConfig.spireModConfig?.apply {
            if (has(k)) {
                r = getString(k)
            } else {
                setString(k, d)
            }
        }
        if (r == null) {
            throw RuntimeException("property is null")
        }
        return PropertyString(r!!, d)
    }

    private fun getConfigurablePropertyKey(name: String): String {
        return "${id}:${name}"
    }


    init {
        relicStrings?.apply {
            title = relicStrings.NAME
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

    open fun isActive(combo: ArrayList<String>): Boolean {
        return combo.size >= numberToActive
    }


    open fun getTip(idToHighlight: ArrayList<String> = arrayListOf()): PowerTip {
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

    fun updateTip(idToHighlight: ArrayList<String> = arrayListOf()) {
        val map: HashMap<String, Int> = hashMapOf()
        idToHighlight.forEach {
            map[it] = map.getOrDefault(it, 0) + 1
        }
        relicStrings?.apply {
            val c = if (idToHighlight.size >= numberToActive) "#g" else "#y"
            desc = relicStrings.DESCRIPTIONS.joinToString(" NL ") {
                var replace = it.replace(" !N! ", " ${c}${numberToActive} ")
                configurablePropertySet.values.forEach { p ->
                    if (p.type == ConfigurableType.Int) {
                        val v = getConfigurableProperty(p.name).toInt()
                        var color = "#y"
                        if (v > p.default.toString().toInt()) color =
                            "#g" else if (v < p.default.toString().toInt()) color = "#r"
                        replace = replace.replace(" !${p.name}! ", " ${color}${v} ")
                    }
                }
                replace
            }
            val s = combo.joinToString(separator = " NL ") {
                var color = "#b"
                val number = map[it]
                var postfix = ""

                if (number != null) {
                    color = "#g"
                    if (number > 1) {
                        postfix = " x${number}"
                    }
                }

                "· ${
                    CardCrawlGame.languagePack.getRelicStrings(it).NAME.split(" ")
                        .joinToString(prefix = color, separator = " $color", postfix = postfix)
                }"
            }
            tip = ComboPowerTip(title, "$s NL $desc", id)
        }
    }

    fun getCurrentComboSize(): Int {
        return currentComboSet[this]?.size ?: 0
    }

    fun getExtraCollectCount(): Int {
        return max(0, getCurrentComboSize() - numberToActive)
    }

    companion object {
        const val NumberToActive = "NumberToActive"
        const val EnableCombo = "EnableCombo"
        const val CanRelicRepeat = "CanRelicRepeat"


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
            val map = registeredComboSet.map { it.javaClass.newInstance() }
            registeredComboSet.clear()
            registeredComboSet.addAll(map)
            enabledRelicComboSets.clear()
            allId.clear()
            val filter: ArrayList<AbstractRelicCombo> = arrayListOf()

            registeredComboSet.forEach { s ->
                if (s.enableCombo) {
                    filter.add(s.javaClass.newInstance())
                }
                s.combo.forEach {
                    RelicLibrary.getRelic(it).tips.removeIf { tip -> tip is ComboPowerTip }
                }
            }
            enabledRelicComboSets.addAll(filter)
            enabledRelicComboSets.forEach { s ->
                s.combo.forEach { id ->
                    val relic = RelicLibrary.getRelic(id)
                    relic?.tips?.add(s.getTip(arrayListOf(id)))
                    val tags = RelicComboFieldPatch.Fields.relicComboTags.get(relic)
                    tags.add(s.id)
                }
                allId.addAll(s.combo)
            }
        }


        // player current combo set
        val currentComboSet: HashMap<AbstractRelicCombo, ArrayList<String>> = hashMapOf()

        // default combo sets
        fun getAllDefaultComboSet(): ArrayList<AbstractRelicCombo> {
            return arrayListOf(
                Alchemy(),
                AncientCreation(),
                Awakened(),
                BringInTheBucks(),
                ChallengeMe(),
                Counterattack(),
                DeepMeditation(),
                Dispel(),
                Eternal(),
                FourElement(),
                GetSomeSleep(),
                Innocence(),
                Bookworm(),
                KungFu(),
                LessIsMore(),
                Library(),
                Mechanism(),
                DarkOnesOwnLuck(),
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
                val relics = AbstractDungeon.player.relics
                val relicIds = relics.map { it.relicId }
                "update combo set start".toLog()
//              clean all effects for all combos
                PatchEffect.cleanAllComboEffect()

//              remove non active combo
                if (!isInCombat()) {
                    currentComboSet.clear()
                } else {
                    val filter = currentComboSet.filter { it.key.isActive(it.value) }
                    currentComboSet.clear()
                    currentComboSet.putAll(filter)
                    currentComboSet.forEach { (_, v) ->
                        v.clear()
                    }
                }
                //update set
                relics.forEach { r ->
                    val tags = RelicComboFieldPatch.Fields.relicComboTags.get(RelicLibrary.getRelic(r.relicId))
                    r.tips.removeIf { it is ComboPowerTip }
                    tags.forEach { t ->
                        val register = enabledRelicComboSets.find { it.id == t }
                        if (register != null) {
                            val find = currentComboSet.entries.find { it.key.id == t }
                            val k = find?.key ?: register.javaClass.newInstance()
                            var v: ArrayList<String> = find?.value ?: arrayListOf()
                            v.add(r.relicId)
                            if (!k.canRelicRepeat) {
                                v = v.distinct() as ArrayList<String>
                            }
                            currentComboSet[k] = v
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

    class Fraction(var dividend: Float, var divisor: Float) {
        fun calc(): Float {
            return dividend / divisor
        }
    }

    fun getChance(fraction: Fraction, caller: Any = this): Float {
        fraction.calc().toLog("pre get chance")
        PatchEffect.onGetChanceSubscribers.forEach { it.effect(fraction, caller) }
        return fraction.calc().apply { toLog("post get chance") }

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

