package combo

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction
import com.megacrit.cardcrawl.cards.AbstractCard.CardType
import com.megacrit.cardcrawl.cards.tempCards.Shiv
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.addToBot
import utils.makeId

class Ninja :
    AbstractRelicCombo(
        Ninja::class.makeId(),
        hashSetOf(Kunai.ID, Shuriken.ID, NinjaScroll.ID, WristBlade.ID, OrnamentalFan.ID)
    ) {
    private val max = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()
    private var maxTriggerCount = max
    private val increase = setConfigurableProperty("M2", 2, ConfigurableType.Int).toInt()
    private val add = setConfigurableProperty("M3", 1, ConfigurableType.Int).toInt()
    private var triggerCount = 0
    private var attackCount = 0
    private var increaseDamage = 0
    override fun onActive() {
        PatchEffect.onPostBattleStartCleanupSubscribers.add(ComboEffect(caller = this) {
            maxTriggerCount = max + getExtraCollectCount()
            increaseDamage = 0
        })
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect(caller = this) {
            triggerCount = 0
            attackCount = 0
        })
        PatchEffect.onPostUseCardSubscribers.add(ComboEffect(caller = this) { c, t ->
            if (c.type == CardType.ATTACK && triggerCount < maxTriggerCount) {
                attackCount++
                if (attackCount == 3) {
                    attackCount = 0
                    triggerCount++
                    addToBot(
                        MakeTempCardInHandAction(Shiv().apply { upgrade() }, add),
                    )
                    increaseDamage += increase
                    flash()
                }
            }
        })
        PatchEffect.onCalculateCardDamageSubscriber.add(ComboEffect(caller = this) { c, d, _ ->
            if (!(c.cost < 0 || c.costForTurn < 0 || c.type != CardType.ATTACK) && (c.costForTurn == 0 || c.freeToPlayOnce)) d + increaseDamage else d
        })
    }

}