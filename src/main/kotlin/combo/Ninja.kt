package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction
import com.megacrit.cardcrawl.cards.AbstractCard.CardType
import com.megacrit.cardcrawl.cards.tempCards.Shiv
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AccuracyPower
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.addToBot
import utils.makeId

class Ninja :
    AbstractRelicCombo(
        Ninja::class.makeId(),
        hashSetOf(Kunai.ID, Shuriken.ID, NinjaScroll.ID, WristBlade.ID, OrnamentalFan.ID)
    ) {
    private var maxTriggerCount = 1
    private var triggerCount = 0
    private var attackCount = 0
    override fun onActive() {
        PatchEffect.onPostBattleStartCleanupSubscribers.add(ComboEffect {
            maxTriggerCount = 1 + getExtraCollectCount()
        })
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect {
            triggerCount = 0
            attackCount = 0
        })
        PatchEffect.onPostUseCardSubscribers.add(ComboEffect { c, t ->
            if (c.type == CardType.ATTACK && triggerCount < maxTriggerCount) {
                attackCount++
                if (attackCount == 3) {
                    attackCount = 0
                    triggerCount++
                    addToBot(
                        MakeTempCardInHandAction(Shiv().apply { upgrade() }, 1),
                        ApplyPowerAction(
                            AbstractDungeon.player,
                            AbstractDungeon.player,
                            AccuracyPower(AbstractDungeon.player, 2)
                        )
                    )
                    flash()
                }
            }
        })
    }

}