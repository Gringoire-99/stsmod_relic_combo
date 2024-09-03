package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.shop.ShopScreen
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect
import utils.makeId

class VIP : AbstractRelicCombo(
    VIP::class.makeId(),
    hashSetOf(MealTicket.ID, SmilingMask.ID, OldCoin.ID, MembershipCard.ID, Courier.ID)
) {
    override fun onAfterShopInit(
        shop: ShopScreen, coloredCards: ArrayList<AbstractCard>?,
        colorlessCards: ArrayList<AbstractCard>?, combo: HashSet<String>
    ) {
        shop.applyDiscount(1 - combo.size * 0.05F, true)
        ShopScreen.actualPurgeCost = 0
    }

    override fun onBeforeShopInit(
        shop: ShopScreen,
        coloredCards: ArrayList<AbstractCard>?,
        colorlessCards: ArrayList<AbstractCard>?,
        combo: HashSet<String>
    ) {
        colorlessCards?.onEach {
            if (it.canUpgrade()) {
                it.upgrade()
            }
        }
        coloredCards?.onEach {
            if (it.canUpgrade()) {
                it.upgrade()
            }
        }
    }

    override fun onBeforeShopPurge(combo: HashSet<String>) {
        ShopScreen.actualPurgeCost = 0
    }

    override fun onAfterShopPurge(combo: HashSet<String>) {
        ShopScreen.actualPurgeCost = 0
        AbstractDungeon.effectsQueue.add(EmptyEffect {
            AbstractDungeon.player.masterDeck.group.filter { it.canUpgrade() }.randomOrNull()?.apply {
                upgrade()
                AbstractDungeon.effectsQueue.add(ShowCardBrieflyEffect(makeStatEquivalentCopy()))
                AbstractDungeon.topLevelEffectsQueue.add(
                    UpgradeShineEffect(
                        Settings.WIDTH.toFloat() / 2.0f,
                        Settings.HEIGHT.toFloat() / 2.0f
                    )
                )
            }
        })
    }


}