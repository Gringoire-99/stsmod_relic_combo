package patch

import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.utility.ScryAction
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.EventHelper
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.random.Random
import com.megacrit.cardcrawl.rewards.chests.AbstractChest
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.CampfireUI
import com.megacrit.cardcrawl.saveAndContinue.SaveFile
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption
import com.megacrit.cardcrawl.ui.panels.PotionPopUp
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect
import combo.AbstractRelicCombo
import utils.logger

class ComboHookPatch {
    /**
     *  应用战斗开始的逻辑
     */
    @SpirePatch2(clz = AbstractRoom::class, method = "update")
    internal class StartOfCombat {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 51)
            fun insert() {
                logger.info("============ Battle Start ===========")
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onBattleStartCleanup(v)
                        k.onBattleStart(v)
                    }
                }
            }
        }
    }

    /**
     *  修改随机药水的稀有度
     */
    @SpirePatch2(clz = AbstractDungeon::class, method = "returnRandomPotion", paramtypez = [Boolean::class])
    internal class returnRandomPotion {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 1, localvars = ["roll"])
            fun insert(@ByRef roll: IntArray) {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onDropRandomPotion(roll, v)
                    }
                }
            }
        }
    }

    /**
     * 触发使用药水后的逻辑1
     */
    @SpirePatch2(clz = PotionPopUp::class, method = "updateTargetMode")
    internal class updateTargetMode {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 34)
            fun insert() {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onAfterUsePotion(v)
                    }
                }
            }
        }
    }

    /**
     * 触发使用药水后的逻辑2
     */
    @SpirePatch2(clz = PotionPopUp::class, method = "updateInput")
    internal class updateInput {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 23)
            fun insert() {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onAfterUsePotion(v)
                    }
                }
            }
        }
    }

    /**
     *  应用能力时触发的效果
     */
    @SpirePatch2(clz = ApplyPowerAction::class, method = "update")
    internal class applyPower {
        companion object {
            @JvmStatic
            @SpirePrefixPatch
            fun prefix(
                ___target: AbstractCreature?,
                ___source: AbstractCreature?,
                ___powerToApply: AbstractPower?,
                @ByRef ___startingDuration: FloatArray,
                @ByRef ___duration: FloatArray
            ) {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v) && ___duration[0] == ___startingDuration[0]) {
                        val shouldContinue =
                            k.onApplyPower(v, target = ___target, source = ___source, power = ___powerToApply)
                        if (!shouldContinue) {
                            ___duration[0] = ___duration[0] - Gdx.graphics.deltaTime
                            SpireReturn.Return()
                        }
                    }
                }
            }
        }
    }

    /**
     * 当玩家受到伤害时触发
     */
    @SpirePatch2(clz = AbstractPlayer::class, method = "damage")
    internal class onTakingDamage {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 61, localvars = ["damageAmount"])
            fun insert(@ByRef damageAmount: IntArray) {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onPlayerTakingDamageFinal(damageAmount, v)
                    }
                }
            }
        }
    }

    /**
     *  战斗结束后执行的逻辑
     */
    @SpirePatch2(clz = AbstractRoom::class, method = "endBattle")
    internal class endBattle {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun post(__instance: AbstractRoom?) {
                logger.info("============ Battle End ===========")
                __instance?.apply {
                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                        if (k.isActive(v)) {
                            k.onEndBattle(this, v)
                        }
                    }
                }
            }
        }
    }

    /**
     * 获得金币时触发的逻辑
     */
    @SpirePatch2(clz = AbstractPlayer::class, method = "gainGold")
    internal class gainGold {
        companion object {
            @JvmStatic
            @SpirePrefixPatch
            fun insert(@ByRef amount: IntArray) {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onGainGold(amount, v)
                    }
                }
            }
        }
    }

    /**
     * 回合开始时触发的逻辑
     */
    @SpirePatch2(clz = AbstractRoom::class, method = "update")
    internal class getNextAction1 {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 65)
            fun insert() {
                logger.info("============ Turn Start ===========")
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onStartOfTurn(v)
                    }
                }
            }
        }
    }

    @SpirePatch2(clz = GameActionManager::class, method = "getNextAction")
    internal class getNextAction2 {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 241)
            fun insert() {
                logger.info("============ Turn Start ===========")
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onStartOfTurn(v)
                    }
                }
            }
        }
    }

    /**
     * 怪物受到伤害触发的逻辑
     */
    @SpirePatch2(clz = AbstractMonster::class, method = "damage")
    internal class onMonsterTakingDamage {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 6, localvars = ["damageAmount"])
            fun insert(info: DamageInfo?, @ByRef damageAmount: IntArray) {
                info?.apply {
                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                        if (k.isActive(v)) {
                            k.onMonsterTakingDamageStart(this, damageAmount, v)
                        }
                    }
                }
            }
        }
    }

    /**
     * 改变姿态前触发的逻辑
     */
    @SpirePatch2(clz = ChangeStanceAction::class, method = "update")
    internal class changeStance {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 38, localvars = ["oldStance"])
            fun prefix(oldStance: AbstractStance?, ___newStance: AbstractStance?) {
                logger.info("========= Change stance old: ${oldStance} new :${___newStance}============")
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onChangeStance(oldStance = oldStance, ___newStance, v)
                    }
                }
            }
        }
    }


    /**
     * 抽到卡片时触发
     */
    @SpirePatch2(clz = AbstractPlayer::class, method = "draw", paramtypez = [Int::class])
    internal class onDraw {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 18, localvars = ["c"])
            fun insert(c: AbstractCard?) {
                c?.apply {
                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                        if (k.isActive(v)) {
                            k.onDrawCard(this, v)
                        }
                    }
                }
            }
        }
    }

    /**
     * 消耗卡片后触发
     */
    @SpirePatch2(clz = CardGroup::class, method = "moveToExhaustPile")
    internal class AfterExhaust {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun postfix(c: AbstractCard?) {
                c?.apply {
                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                        if (k.isActive(v)) {
                            k.onExhaustCard(c, v)
                        }
                    }
                }
            }
        }
    }

    /**
     * 休息时触发
     */
    @SpirePatch2(clz = CampfireSleepEffect::class, method = "update")
    internal class beforeSleep {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 11)
            fun postfix(@ByRef ___healAmount: IntArray) {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onRest(___healAmount, v)
                    }
                }
            }
        }
    }

    /**
     * 出牌时触发
     */
    @SpirePatch2(clz = AbstractPlayer::class, method = "useCard")
    internal class useCard {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun postfix(c: AbstractCard?, monster: AbstractMonster?, energyOnUse: Int) {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onUseCard(c, monster, energyOnUse, v)
                    }
                }
            }
        }
    }

    /**
     * 打开箱子后触发
     */
    @SpirePatch2(clz = AbstractChest::class, method = "open")
    internal class openChest {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 4)
            fun insert(__instance: AbstractChest?) {
                __instance?.apply {
                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                        if (k.isActive(v)) {
                            k.onOpenChest(__instance, v)
                        }
                    }
                }
            }
        }
    }

    /**
     * 预知时触发
     */
    @SpirePatch2(clz = ScryAction::class, method = "update")
    internal class onScry {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 9)
            fun insert(@ByRef ___amount: IntArray) {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onScry(___amount, v)
                    }
                }
            }
        }
    }

    /**
     * 移动到下个房间触发
     */
    @SpirePatch2(clz = AbstractDungeon::class, method = "nextRoomTransition", paramtypez = [SaveFile::class])
    internal class nextRoomTransition {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 156)
            fun insert() {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onNextRoom(v)
                    }
                }
            }
        }
    }

    @SpirePatch2(clz = CampfireUI::class, method = "initializeButtons")
    internal class initializeButtons {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun insert(___buttons: ArrayList<AbstractCampfireOption>?) {
                ___buttons?.apply {
                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                        if (k.isActive(v)) {
                            k.onInitCampfireUI(this, v)
                        }
                    }
                }
            }
        }
    }

//    @SpirePatch2(clz = CampfireUI::class, method = "updateTouchscreen")
//    internal class updateTouchscreen {
//        companion object {
//            @JvmStatic
//            @SpireInsertPatch(rloc = 13)
//            fun insert() {
//                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
//                    if (k.isActive(v)) {
//                        k.onUseCampfireOption(v)
//                    }
//                }
//            }
//        }
//    }

    /**
     *  随机事件时触发
     */
    @SpirePatch2(clz = EventHelper::class, method = "roll", paramtypez = [Random::class])
    internal class onRollEvent {
        companion object {
            @JvmStatic
            @SpireInsertPatch(
                rloc = 43,
                localvars = ["forceChest", "eliteSize", "monsterSize", "shopSize", "treasureSize"]
            )
            fun insert(
                @ByRef forceChest: BooleanArray,
                @ByRef eliteSize: IntArray,
                @ByRef monsterSize: IntArray,
                @ByRef shopSize: IntArray,
                @ByRef treasureSize: IntArray
            ) {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onRollEvent(
                            forceChest = forceChest,
                            eliteSize = eliteSize,
                            monsterSize = monsterSize,
                            shopSize = shopSize,
                            treasureSize = treasureSize, v
                        )
                    }
                }
            }
        }
    }
}