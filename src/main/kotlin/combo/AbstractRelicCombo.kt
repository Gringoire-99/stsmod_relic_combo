package combo

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.rewards.chests.AbstractChest
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect

/**
 *  TODO 修改以支持持久化 (目前一次性触发的效果会因为sl导致多次触发)
 *
 *  TODO:FIX !WARN! 目前存在的问题：获取遗物/删除遗物会使combo重新计算，在钩子里获取遗物/删除遗物会触发并发修改的异常。
 */
abstract class AbstractRelicCombo(
    val id: String,
    val combo: HashSet<String> = hashSetOf(),
    val numberToActive: Int = 3
) {
    val title: String
    val desc: String
    private var tip: PowerTip
    val flavorText: String?
    private var isTriggeredActiveEffect: Boolean = false

    init {
        val relicStrings = CardCrawlGame.languagePack.getRelicStrings(id)
        title = relicStrings.NAME
        desc = relicStrings.DESCRIPTIONS.joinToString(" NL ") { it.replace(" !N! ", numberToActive.toString()) }
        flavorText = relicStrings.FLAVOR
        tip = PowerTip(title, desc)
        combo.removeIf { !RelicLibrary.isARelic(it) }
        updateTip()
    }

    fun addCombo(vararg relicId: String) {
        val filter = relicId.filter { !RelicLibrary.isARelic(it) }
        combo.addAll(filter)
        updateTip()
    }

    fun removeCombo(vararg relicId: String) {
        combo.removeIf {
            it in relicId
        }
        updateTip()
    }

    fun exist(relicId: String): Boolean {
        return combo.contains(relicId)
    }


    open fun onBattleStart(combo: HashSet<String>) {

    }

    open fun onBattleStartCleanup(combo: HashSet<String>) {

    }

    open fun onDropRandomPotion(chance: IntArray, combo: HashSet<String>) {

    }

    open fun onObtainRelic(r: AbstractRelic, combo: HashSet<String>) {}
    open fun onAfterUsePotion(combo: HashSet<String>) {

    }

    open fun onPlayerTakingDamageFinal(damage: IntArray, combo: HashSet<String>) {

    }

    open fun onMonsterTakingDamageStart(info: DamageInfo, damageAmount: IntArray, combo: HashSet<String>) {

    }

    open fun onApplyPower(
        combo: HashSet<String>,
        target: AbstractCreature?,
        source: AbstractCreature?,
        power: AbstractPower?
    ): Boolean {
        return true
    }

    open fun onExhaustCard(c: AbstractCard, combo: HashSet<String>) {

    }

    /**
     * 判断是否能触发组合效果，返回为真时才能触发
     */
    open fun isActive(combo: HashSet<String>): Boolean {
        return combo.size >= numberToActive && AbstractDungeon.currMapNode != null
    }


    open fun onEndBattle(room: AbstractRoom, combo: HashSet<String>) {

    }

    open fun onGainGold(amount: IntArray, combo: HashSet<String>) {

    }

    open fun onChangeStance(oldStance: AbstractStance?, newStance: AbstractStance?, combo: HashSet<String>) {

    }


    open fun onDrawCard(c: AbstractCard, combo: HashSet<String>) {

    }

    open fun onStartOfTurn(combo: HashSet<String>) {}

    open fun onRest(healAmount: IntArray, combo: HashSet<String>) {

    }

    open fun onUseCard(c: AbstractCard?, monster: AbstractMonster?, energyOnUse: Int, combo: HashSet<String>) {

    }

    open fun onOpenChest(c: AbstractChest, combo: HashSet<String>) {

    }

    open fun onRollEvent(
        forceChest: BooleanArray,
        eliteSize: IntArray,
        monsterSize: IntArray,
        shopSize: IntArray,
        treasureSize: IntArray, combo: HashSet<String>
    ) {

    }

    open fun onScry(amount: IntArray, combo: HashSet<String>) {

    }

    open fun getTip(idToHighlight: HashSet<String> = hashSetOf()): PowerTip {
        updateTip(idToHighlight)
        return tip
    }

    open fun onNextRoom(combo: HashSet<String>) {}
    open fun onInitCampfireUI(buttons: ArrayList<AbstractCampfireOption>, combo: HashSet<String>) {

    }

    open fun onUseCampfireOption(combo: HashSet<String>) {}

    /**
     * 在战斗外使用有可能触发并发修改的异常，不清楚是什么原因导致的
     */
    open fun showText() {
        val msg = if (flavorText == null || flavorText == "") title else flavorText
        val textAboveCreatureEffect = TextAboveCreatureEffect(
            AbstractDungeon.player.hb.cX - AbstractDungeon.player.animX,
            AbstractDungeon.player.hb.cY + AbstractDungeon.player.hb.height / 2.0f,
            msg, Color.GOLD.cpy()
        )
        AbstractDungeon.topLevelEffects.add(
            textAboveCreatureEffect
        )
    }

    private fun updateTip(idToHighlight: HashSet<String> = hashSetOf()) {
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
        tip = PowerTip(title, "$s NL $desc")
    }

    companion object {
        val relicComboSets = hashSetOf<AbstractRelicCombo>()
        private val allId = hashSetOf<String>()
        val currentComboSet: HashMap<AbstractRelicCombo, HashSet<String>> = hashMapOf()

        fun registerComboSet(vararg comboSet: AbstractRelicCombo) {
            relicComboSets.addAll(comboSet)
            relicComboSets.forEach { s ->
                s.combo.forEach { id ->
                    val relic = RelicLibrary.getRelic(id)
                    if (relic != null) {
                        relic.tips.removeIf { it.header == s.tip.header }
                        relic.tips.add(s.getTip(hashSetOf(id)))
                    }
                }
                allId.addAll(s.combo)
            }
        }

        fun updateCombo() {
            if (AbstractDungeon.player != null) {
                val currentRelics: HashSet<String> = AbstractDungeon.player.relics.map { it.relicId }.toHashSet()
                currentComboSet.forEach { (_, v) ->
                    v.removeIf { !currentRelics.contains(it) }
                }
                AbstractDungeon.player.relics.forEach { r ->
                    if (r.relicId in allId) {
                        relicComboSets.forEach { s ->
                            if (r.relicId in s.combo) {
                                // 更新组合集合
                                val find = currentComboSet.entries.find { it.key.id == s.id }
                                val k = find?.key ?: s.javaClass.newInstance()
                                val v = find?.value ?: hashSetOf()
                                v.add(r.relicId)

                                currentComboSet[k] = v
                            }
                        }
                    }
                }

                // 更新描述
                AbstractDungeon.player.relics.forEach { r ->
                    r.tips.removeIf { relicComboSets.any { s -> s.title == it.header } }
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