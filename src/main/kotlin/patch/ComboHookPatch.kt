package patch

import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.utility.ScryAction
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.EventHelper
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.potions.AbstractPotion
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.random.Random
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier
import com.megacrit.cardcrawl.rewards.chests.AbstractChest
import com.megacrit.cardcrawl.rooms.CampfireUI
import com.megacrit.cardcrawl.saveAndContinue.SaveFile
import com.megacrit.cardcrawl.shop.ShopScreen
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption
import com.megacrit.cardcrawl.ui.panels.PotionPopUp
import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect
import core.AbstractRelicCombo
import core.PatchEffect
import utils.toLog

class ComboHookPatch {
//    /**
//     *  应用战斗开始的逻辑
//     */
//    @SpirePatch2(clz = AbstractRoom::class, method = "update")
//    internal class StartOfCombat {
//        companion object {
//            @JvmStatic
//            @SpireInsertPatch(rloc = 51)
//            fun insert() {
//                PatchEffect.onPostBattleStartCleanupSubscribers.forEach { it.effect() }
//                PatchEffect.onPostBattleStartSubscribers.forEach { it.effect() }
//            }
//        }
//    }

    /**
     *  修改随机药水的稀有度
     */
    @SpirePatch2(clz = AbstractDungeon::class, method = "returnRandomPotion", paramtypez = [Boolean::class])
    internal class returnRandomPotion {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 1, localvars = ["roll"])
            fun insert(@ByRef roll: IntArray) {
                "on drop random potion chance:${roll[0]}".toLog()
                var r = roll[0]
                PatchEffect.onPreDropRandomPotionSubscribers.forEach { r = it.effect(r) }
                "actual potion chance:${roll[0]}".toLog()
                roll[0] = r
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
            fun insert(___potion: AbstractPotion?, ___hoveredMonster: AbstractMonster?) {
                "on used potion:${___potion}".toLog()
                ___potion?.apply {
                    PatchEffect.onPostUsePotionSubscribers.forEach { it.effect(___potion, ___hoveredMonster) }
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
            fun insert(___potion: AbstractPotion?, ___hoveredMonster: AbstractMonster?) {
                "on used potion:${___potion}".toLog()
                ___potion?.apply {
                    PatchEffect.onPostUsePotionSubscribers.forEach { it.effect(___potion, ___hoveredMonster) }
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
            ): SpireReturn<Void>? {
                "pre apply power:${___powerToApply}".toLog()
                var shouldContinue = true
                if (___duration[0] == ___startingDuration[0]) {
                    "publish on pre apply power subs".toLog()
                    PatchEffect.onPreApplyPowerSubscribers.forEach {
                        if (shouldContinue) {
                            shouldContinue = it.effect(___target, ___source, ___powerToApply)
                        }
                    }
                }
                if (!shouldContinue) {
                    "break apply power action".toLog()
                    ___duration[0] = ___duration[0] - Gdx.graphics.deltaTime
                    return SpireReturn.Return()
                }
                return SpireReturn.Continue()
            }
        }
    }

    /**
     * 当玩家受到伤害时触发(即将损失血量)
     */
    @SpirePatch2(clz = AbstractPlayer::class, method = "damage")
    internal class onTakingDamage {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 61, localvars = ["damageAmount"])
            fun insert(@ByRef damageAmount: IntArray, info: DamageInfo?) {
                var d = damageAmount[0]
                "on post player taking damage(before lose hp) :${d}".toLog()
                PatchEffect.onPostPlayerTakingDamageSubscribers.forEach { d = it.effect(d, info) }
                "actual player taking damage(before lose hp) $d".toLog()
                damageAmount[0] = d
            }
        }
    }

    /**
     * 当玩家即将损失生命前
     */
    @SpirePatch2(clz = AbstractPlayer::class, method = "damage")
    internal class beforeLostHp {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 99, localvars = ["damageAmount"])
            fun insert(@ByRef damageAmount: IntArray, info: DamageInfo?) {
                var d = damageAmount[0]
                "before lose hp ${d}".toLog()
                PatchEffect.onPrePlayerLoseHpSubscribers.forEach { d = it.effect(d, info) }
                "actual before lose hp ${d}".toLog()
                damageAmount[0] = d
            }
        }
    }

//    /**
//     *  战斗结束后执行的逻辑
//     */
//    @SpirePatch2(clz = AbstractRoom::class, method = "endBattle")
//    internal class endBattle {
//        companion object {
//            @JvmStatic
//            @SpirePostfixPatch
//            fun post(__instance: AbstractRoom?) {
//                __instance?.apply {
//                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
//                        if (k.isActive(v)) {
//                            k.onEndBattle(this, v)
//                        }
//                    }
//                }
//            }
//        }
//    }

    /**
     * 获得金币时触发的逻辑
     */
    @SpirePatch2(clz = AbstractPlayer::class, method = "gainGold")
    internal class gainGold {
        companion object {
            @JvmStatic
            @SpirePrefixPatch
            fun insert(@ByRef amount: IntArray) {
                "on player gain gold ${amount[0]}".toLog()
                var a = amount[0]
                PatchEffect.onPreGainGoldSubscribers.forEach { a = it.effect(a) }
                "actual gold:$a".toLog()
                amount[0] = a
            }
        }
    }

//    /**
//     * 回合开始时触发的逻辑
//     */
//    @SpirePatch2(clz = AbstractRoom::class, method = "update")
//    internal class getNextAction1 {
//        companion object {
//            @JvmStatic
//            @SpireInsertPatch(rloc = 65)
//            fun insert() {
//                logger.info("============ Turn Start ===========")
//                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
//                    if (k.isActive(v)) {
//                        k.onStartOfTurn(v)
//                    }
//                }
//            }
//        }
//    }

//    @SpirePatch2(clz = GameActionManager::class, method = "getNextAction")
//    internal class getNextAction2 {
//        companion object {
//            @JvmStatic
//            @SpireInsertPatch(rloc = 241)
//            fun insert() {
//                logger.info("============ Turn Start ===========")
//                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
//                    if (k.isActive(v)) {
//                        k.onStartOfTurn(v)
//                    }
//                }
//            }
//        }
//    }

    /**
     * 怪物受到伤害触发的逻辑
     */
    @SpirePatch2(clz = AbstractMonster::class, method = "damage")
    internal class onMonsterTakingDamage {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 6, localvars = ["damageAmount"])
            fun insert(info: DamageInfo?, @ByRef damageAmount: IntArray) {
                "on monster taking damage:${damageAmount[0]}".toLog()
                var d = damageAmount[0]
                PatchEffect.onPreMonsterTakingDamageSubscribers.forEach { d = it.effect(d, info) }
                "actual on monster taking damage:${d}".toLog()
                damageAmount[0] = d
            }
        }
    }

    /**
     * 改变姿态后触发的逻辑
     */
    @SpirePatch2(clz = ChangeStanceAction::class, method = "update")
    internal class changeStance {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 38, localvars = ["oldStance"])
            fun prefix(oldStance: AbstractStance?, ___newStance: AbstractStance?) {
                "on post change stance old:${oldStance} new:${___newStance}".toLog()
                PatchEffect.onPostChangeStanceSubscribers.forEach { it.effect(oldStance, ___newStance) }
            }
        }
    }

//
//    /**
//     * 抽到卡片时触发
//     */
//    @SpirePatch2(clz = AbstractPlayer::class, method = "draw", paramtypez = [Int::class])
//    internal class onDraw {
//        companion object {
//            @JvmStatic
//            @SpireInsertPatch(rloc = 18, localvars = ["c"])
//            fun insert(c: AbstractCard?) {
//                c?.apply {
//                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
//                        if (k.isActive(v)) {
//                            k.onDrawCard(this, v)
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 消耗卡片后触发
//     */
//    @SpirePatch2(clz = CardGroup::class, method = "moveToExhaustPile")
//    internal class AfterExhaust {
//        companion object {
//            @JvmStatic
//            @SpirePostfixPatch
//            fun postfix(c: AbstractCard?) {
//                c?.apply {
//                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
//                        if (k.isActive(v)) {
//                            k.onExhaustCard(c, v)
//                        }
//                    }
//                }
//            }
//        }
//    }

    /**
     * 休息时触发
     */
    @SpirePatch2(clz = CampfireSleepEffect::class, method = "update")
    internal class beforeSleep {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 11)
            fun postfix(@ByRef ___healAmount: IntArray) {
                "pre rest heal:${___healAmount[0]}".toLog()
                var h = ___healAmount[0]
                PatchEffect.onPreRestSubscribers.forEach { h = it.effect(h) }
                "actual pre rest heal:${___healAmount[0]}".toLog()
                ___healAmount[0] = h
            }
        }
    }

    /**
     * 出牌后触发
     */
    @SpirePatch2(clz = AbstractPlayer::class, method = "useCard")
    internal class useCard {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun postfix(c: AbstractCard?, monster: AbstractMonster?, energyOnUse: Int) {
                "post use card:${c}".toLog()
                c?.apply {
                    PatchEffect.onPostUseCardSubscribers.forEach { it.effect(c, monster) }
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
                "post open chest".toLog()
                __instance?.apply {
                    PatchEffect.onPostOpenChestSubscribers.forEach { it.effect(this) }
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
                "pre on scry amount:${___amount[0]}".toLog()
                var a = ___amount[0]
                PatchEffect.onPreScrySubscribers.forEach { a = it.effect(a) }
                ___amount[0] = a
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
                "moving to next room".toLog()
                PatchEffect.onPostGoNextRoomSubscribers.forEach { it.effect() }
            }
        }
    }

    @SpirePatch2(clz = CampfireUI::class, method = "initializeButtons")
    internal class initializeButtons {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun insert(___buttons: ArrayList<AbstractCampfireOption>?) {
                "init campfire btn".toLog()
                ___buttons?.apply {
                    PatchEffect.onPostInitCampFireSubscribers.forEach { it.effect(this) }
                }
            }
        }
    }

//    @SpirePatch2(clz = AbstractCampfireOption::class, method = "update")
//    internal class onOptionSelected {
//        companion object {
//            @JvmStatic
//            @SpireInsertPatch(rloc = 26)
//            fun insert(__instance: AbstractCampfireOption?) {
//                __instance?.apply {
//                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
//                        if (k.isActive(v)) {
//                            k.onUseCampfireOption(__instance, v)
//                        }
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
                "pre roll random event".toLog()
                PatchEffect.onPreRollEventSubscribers.forEach {
                    it.effect(
                        forceChest,
                        eliteSize,
                        monsterSize,
                        shopSize,
                        treasureSize
                    )
                }
            }
        }
    }

    @SpirePatch2(clz = AbstractDungeon::class, method = "returnRandomRelic")
    internal class beforeReturnRandomRelic {
        companion object {
            @JvmStatic
            @SpirePrefixPatch
            fun pre(
                tier: RelicTier?
            ): SpireReturn<AbstractRelic?> {
                "pre return random relic tier:${tier}".toLog()
                var t = tier
                t?.apply {
                    PatchEffect.onPreReturnRandomRelicSubscribers.forEach { t = it.effect(this) }
                }
                "actual pre return random relic tier:${t}".toLog()
                if (t == tier) {
                    return SpireReturn.Continue<AbstractRelic?>()
                } else {
                    return SpireReturn.Return(RelicLibrary.getRelic(AbstractDungeon.returnRandomRelicKey(t)).makeCopy())
                }

            }
        }
    }

    @SpirePatch2(clz = ShopScreen::class, method = "init")
    internal class afterShopInit {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun post(
                __instance: ShopScreen?,
                coloredCards: ArrayList<AbstractCard>?,
                colorlessCards: ArrayList<AbstractCard>?,
            ) {
                "post shop init".toLog()
                __instance?.apply {
                    PatchEffect.onPostShopInitSubscribers.forEach { it.effect(this, coloredCards, colorlessCards) }
                }
            }
        }
    }

    @SpirePatch2(clz = ShopScreen::class, method = "init")
    internal class beforeShopInit {
        companion object {
            @JvmStatic
            @SpirePrefixPatch
            fun pre(
                __instance: ShopScreen?,
                coloredCards: ArrayList<AbstractCard>?,
                colorlessCards: ArrayList<AbstractCard>?,
            ) {
                "pre shop init".toLog()
                __instance?.apply {
                    PatchEffect.onPreShopInitSubscribers.forEach { it.effect(this, coloredCards, colorlessCards) }
                }
            }
        }
    }

    @SpirePatch2(clz = ShopScreen::class, method = "purgeCard")
    internal class beforeShopPurgeCard {
        companion object {
            @JvmStatic
            @SpirePrefixPatch
            fun pre(
            ) {
                "pre shop purge".toLog()
                PatchEffect.onPreShopPurgeSubscribers.forEach { it.effect() }
            }
        }
    }

    @SpirePatch2(clz = ShopScreen::class, method = "purgeCard")
    internal class afterShopPurgeCard {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun post(
            ) {
                "post shop purge".toLog()
                PatchEffect.onPostShopPurgeSubscribers.forEach { it.effect() }
            }
        }
    }

    @SpirePatch2(clz = AbstractCard::class, method = "calculateCardDamage")
    internal class BeforeCalculateCardDamage {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 9, localvars = ["tmp"])
            fun insert(__instance: AbstractCard, @ByRef tmp: FloatArray, mo: AbstractMonster?) {
                var d = tmp[0]
                PatchEffect.onCalculateCardDamageSubscriber.forEach {
                    d = it.effect(__instance, d, mo)
                }
                tmp[0] = d
            }
        }
    }

    @SpirePatch2(clz = AbstractCard::class, method = "calculateCardDamage")
    internal class BeforeCalculateCardDamageMulti {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 65, localvars = ["tmp"])
            fun insert(__instance: AbstractCard, tmp: FloatArray, mo: AbstractMonster?) {
                tmp.forEachIndexed { index, _ ->
                    PatchEffect.onCalculateCardDamageSubscriber.forEach {
                        tmp[index] = it.effect(__instance, tmp[index], mo)
                    }
                }
            }
        }
    }
}