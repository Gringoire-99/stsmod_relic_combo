package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.shop.ShopScreen
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.makeId
import kotlin.math.max

class VIP : AbstractRelicCombo(
    VIP::class.makeId(),
    hashSetOf(MealTicket.ID, SmilingMask.ID, OldCoin.ID, MembershipCard.ID, Courier.ID)
) {
    private val repeat = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()
    override fun onActive() {
        PatchEffect.onPreShopInitSubscribers.add(ComboEffect(caller = this) {
                _: ShopScreen,
                coloredCards: ArrayList<AbstractCard>?,
                colorlessCards: ArrayList<AbstractCard>?,
            ->

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
        })
        PatchEffect.onPostShopInitSubscribers.add(ComboEffect(caller = this) { shop: ShopScreen, _: ArrayList<AbstractCard>?,
                                                                               _: ArrayList<AbstractCard>? ->
            shop.applyDiscount(max(0F, 1 - getCurrentComboSize() * 0.25F), true)
            ShopScreen.actualPurgeCost = 0
        })
        PatchEffect.onPreShopPurgeSubscribers.add(ComboEffect(caller = this) {
            ShopScreen.actualPurgeCost = 0
        })
        PatchEffect.onPostShopPurgeSubscribers.add(ComboEffect(caller = this) {
            ShopScreen.actualPurgeCost = 0
            repeat(repeat) {
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
            flash()
        })

    }
}