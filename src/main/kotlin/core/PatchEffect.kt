package core

import basemod.interfaces.*
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.potions.AbstractPotion
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier
import com.megacrit.cardcrawl.rewards.chests.AbstractChest
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.shop.ShopScreen
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption
import utils.toLog

typealias OnPostEndBattle = (room: AbstractRoom) -> Unit
typealias OnPostBattleStart = () -> Unit
typealias OnPostBattleStartCleanup = () -> Unit
typealias OnPreGainGold = (amount: Int) -> Int
typealias OnPostChangeStance = (oldStance: AbstractStance?, newStance: AbstractStance?) -> Unit
typealias OnPostDrawnCard = (c: AbstractCard) -> Unit
typealias OnPostStartOfTurn = () -> Unit
typealias OnPreRest = (healAmount: Int) -> Int
typealias OnPostUseCard = (c: AbstractCard, target: AbstractCreature?) -> Unit
typealias OnPreDropRandomPotion = (change: Int) -> Int
typealias OnPostObtainRelic = (r: AbstractRelic) -> Unit
typealias OnPostUsePotion = (potion: AbstractPotion, target: AbstractMonster?) -> Unit
typealias OnPostPlayerTakingDamage = (damage: Int, info: DamageInfo?) -> Int
typealias OnPrePlayerTakingDamage = (damage: Int, info: DamageInfo?) -> Int
typealias OnPrePlayerLoseHp = (damage: Int, info: DamageInfo?) -> Int
typealias OnPreMonsterTakingDamage = (damage: Int, info: DamageInfo?) -> Int
typealias OnPreApplyPower = (target: AbstractCreature?, source: AbstractCreature?, power: AbstractPower?) -> Boolean
typealias OnPostApplyPower = (target: AbstractCreature?, source: AbstractCreature?, power: AbstractPower?) -> Boolean
typealias OnPostExhaustCard = (c: AbstractCard) -> Unit
typealias OnPostOpenChest = (c: AbstractChest) -> Unit
typealias OnPreRollEvent = (
    forceChest: BooleanArray,
    eliteSize: IntArray,
    monsterSize: IntArray,
    shopSize: IntArray,
    treasureSize: IntArray,
) -> Unit

typealias OnPreScry = (amount: Int) -> Int
typealias OnPostGoNextRoom = () -> Unit
typealias OnPostInitCampFire = (buttons: ArrayList<AbstractCampfireOption>) -> Unit
typealias OnPostUseCampFireOption = (option: AbstractCampfireOption) -> Unit
typealias OnPreReturnRandomRelic = (tier: RelicTier) -> RelicTier?
typealias OnPreShopInit = (
    shop: ShopScreen,
    coloredCards: ArrayList<AbstractCard>?,
    colorlessCards: ArrayList<AbstractCard>?,
) -> Unit

typealias OnPostShopInit = (
    shop: ShopScreen,
    coloredCards: ArrayList<AbstractCard>?,
    colorlessCards: ArrayList<AbstractCard>?,
) -> Unit

typealias OnPreShopPurge = () -> Unit
typealias OnPostShopPurge = () -> Unit
typealias OnGetChance = (fraction: AbstractRelicCombo.Fraction, caller: Any) -> Unit
typealias OnCalculateCardDamage = (card: AbstractCard, damage: Float, mo: AbstractMonster?) -> Float

/**
 * combo's effect
 *
 * update when combo set was updated
 *
 */
class PatchEffect : OnPlayerTurnStartPostDrawSubscriber, OnStartBattleSubscriber, PostDrawSubscriber,
    PostBattleSubscriber, OnPlayerDamagedSubscriber, PostPowerApplySubscriber, PostExhaustSubscriber,
    StartGameSubscriber {
    companion object {
        val patchInstance = PatchEffect()
        val onPostEndBattleSubscribers: ArrayList<Effect<OnPostEndBattle>> = arrayListOf()
        val onPostBattleStartSubscribers: ArrayList<Effect<OnPostBattleStart>> = arrayListOf()
        val onPostBattleStartCleanupSubscribers: ArrayList<Effect<OnPostBattleStartCleanup>> = arrayListOf()
        val onPreGainGoldSubscribers: ArrayList<Effect<OnPreGainGold>> = arrayListOf()
        val onPostChangeStanceSubscribers: ArrayList<Effect<OnPostChangeStance>> = arrayListOf()
        val onPostDrawnCardSubscribers: ArrayList<Effect<OnPostDrawnCard>> = arrayListOf()
        val onPostStartOfTurnSubscribers: ArrayList<Effect<OnPostStartOfTurn>> = arrayListOf()
        val onPreRestSubscribers: ArrayList<Effect<OnPreRest>> = arrayListOf()
        val onPostUseCardSubscribers: ArrayList<Effect<OnPostUseCard>> = arrayListOf()
        val onPreDropRandomPotionSubscribers: ArrayList<Effect<OnPreDropRandomPotion>> = arrayListOf()
        val onPostObtainRelicSubscribers: ArrayList<Effect<OnPostObtainRelic>> = arrayListOf()
        val onPostUsePotionSubscribers: ArrayList<Effect<OnPostUsePotion>> = arrayListOf()
        val onPostPlayerTakingDamageSubscribers: ArrayList<Effect<OnPostPlayerTakingDamage>> = arrayListOf()
        val onPrePlayerTakingDamageSubscribers: ArrayList<Effect<OnPrePlayerTakingDamage>> = arrayListOf()
        val onPrePlayerLoseHpSubscribers: ArrayList<Effect<OnPrePlayerLoseHp>> = arrayListOf()
        val onPreMonsterTakingDamageSubscribers: ArrayList<Effect<OnPreMonsterTakingDamage>> = arrayListOf()
        val onPreApplyPowerSubscribers: ArrayList<Effect<OnPreApplyPower>> = arrayListOf()
        val onPostApplyPowerSubscribers: ArrayList<Effect<OnPostApplyPower>> = arrayListOf()
        val onPostExhaustCardSubscribers: ArrayList<Effect<OnPostExhaustCard>> = arrayListOf()
        val onPostOpenChestSubscribers: ArrayList<Effect<OnPostOpenChest>> = arrayListOf()
        val onPreRollEventSubscribers: ArrayList<Effect<OnPreRollEvent>> = arrayListOf()
        val onPreScrySubscribers: ArrayList<Effect<OnPreScry>> = arrayListOf()
        val onPostGoNextRoomSubscribers: ArrayList<Effect<OnPostGoNextRoom>> = arrayListOf()
        val onPostInitCampFireSubscribers: ArrayList<Effect<OnPostInitCampFire>> = arrayListOf()
        val onPostUseCampFireOptionSubscribers: ArrayList<Effect<OnPostUseCampFireOption>> = arrayListOf()
        val onPreReturnRandomRelicSubscribers: ArrayList<Effect<OnPreReturnRandomRelic>> = arrayListOf()
        val onPreShopInitSubscribers: ArrayList<Effect<OnPreShopInit>> = arrayListOf()
        val onPostShopInitSubscribers: ArrayList<Effect<OnPostShopInit>> = arrayListOf()
        val onPreShopPurgeSubscribers: ArrayList<Effect<OnPreShopPurge>> = arrayListOf()
        val onPostShopPurgeSubscribers: ArrayList<Effect<OnPostShopPurge>> = arrayListOf()
        val onGetChanceSubscribers: ArrayList<Effect<OnGetChance>> = arrayListOf()
        val onCalculateCardDamageSubscriber: ArrayList<Effect<OnCalculateCardDamage>> = arrayListOf()
        val listOfAll = arrayListOf(
            onPostEndBattleSubscribers,
            onPostBattleStartSubscribers,
            onPostBattleStartCleanupSubscribers,
            onPreGainGoldSubscribers,
            onPostChangeStanceSubscribers,
            onPostDrawnCardSubscribers,
            onPostStartOfTurnSubscribers,
            onPreRestSubscribers,
            onPostUseCardSubscribers,
            onPreDropRandomPotionSubscribers,
            onPostObtainRelicSubscribers,
            onPostUsePotionSubscribers,
            onPostPlayerTakingDamageSubscribers,
            onPrePlayerTakingDamageSubscribers,
            onPreMonsterTakingDamageSubscribers,
            onPreApplyPowerSubscribers,
            onPostApplyPowerSubscribers,
            onPostExhaustCardSubscribers,
            onPostOpenChestSubscribers,
            onPreRollEventSubscribers,
            onPreScrySubscribers,
            onPostGoNextRoomSubscribers,
            onPostInitCampFireSubscribers,
            onPostUseCampFireOptionSubscribers,
            onPreReturnRandomRelicSubscribers,
            onPreShopInitSubscribers,
            onPostShopInitSubscribers,
            onPreShopPurgeSubscribers,
            onPostShopPurgeSubscribers,
            onPrePlayerLoseHpSubscribers,
            onGetChanceSubscribers,
            onCalculateCardDamageSubscriber
        )

        fun cleanNonInnateEffect() {
            listOfAll.forEach { s ->
                s.removeIf { !it.isInnate }
            }
        }

        fun cleanAllComboEffect() {
            "clean up all combo effects".toLog()
            listOfAll.forEach { s ->
                s.removeIf { it is ComboEffect }
            }
        }

        fun sortByPriority() {
            listOfAll.forEach { s ->
                s.sortBy { -it.priority }
            }
        }
    }


    override fun receiveOnPlayerTurnStartPostDraw() {
        "player turn start post draw".toLog()
        onPostStartOfTurnSubscribers.forEach { it.effect() }
    }


    override fun receiveOnBattleStart(p0: AbstractRoom?) {
        "on battle start".toLog()
        onPostBattleStartCleanupSubscribers.forEach { it.effect() }
        onPostBattleStartSubscribers.forEach { it.effect() }
    }

    override fun receivePostDraw(c: AbstractCard?) {
        "post draw card:${c}".toLog()
        c?.apply {
            onPostDrawnCardSubscribers.forEach { it.effect(c) }
        }
    }

    override fun receivePostBattle(r: AbstractRoom?) {
        "post battle".toLog()
        r?.apply {
            onPostEndBattleSubscribers.forEach { it.effect(r) }
        }
    }

    /**
     * before lose block
     */
    override fun receiveOnPlayerDamaged(damage: Int, info: DamageInfo?): Int {
        "pre player damaged(before lose block) $damage".toLog()
        var d = damage
        onPrePlayerTakingDamageSubscribers.forEach { d = it.effect(d, info) }
        return d
    }

    override fun receivePostPowerApplySubscriber(p: AbstractPower?, t: AbstractCreature?, s: AbstractCreature?) {
        "post apply power $p".toLog()
        onPostApplyPowerSubscribers.forEach { it.effect(t, s, p) }
    }

    override fun receivePostExhaust(c: AbstractCard?) {
        "post exhaust card:$c".toLog()
        c?.apply {
            onPostExhaustCardSubscribers.forEach { it.effect(c) }
        }
    }

    override fun receiveStartGame() {
        "start game update relic combo".toLog()
        AbstractRelicCombo.updateCombo()
    }


}

class ComboEffect<T : Function<Any?>>(priority: Int = 1, effect: T) :
    Effect<T>(effect = effect, priority = priority)

abstract class Effect<T : Function<Any?>>(val priority: Int = 0, val isInnate: Boolean = false, val effect: T)
