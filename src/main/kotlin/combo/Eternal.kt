package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.addToTop
import utils.makeId
import kotlin.math.max

class Eternal :
    AbstractRelicCombo(
        Eternal::class.makeId(),
        hashSetOf(
            MercuryHourglass.ID,
            Sundial.ID,
            StoneCalendar.ID,
            UnceasingTop.ID,
            IncenseBurner.ID,
            RunicPyramid.ID,
            Pocketwatch.ID
        ),
    ) {

    private var counter = 0
    private val numberToTrigger = 7

    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect {
            addToTop(
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    StrengthPower(AbstractDungeon.player, 1)
                ),
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    DexterityPower(AbstractDungeon.player, 1)
                )
            )
            flash()
        })
        PatchEffect.onPostBattleStartCleanupSubscribers.add(ComboEffect {
            counter = 0
        })
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect {
            counter += 1
            val require = max(1, numberToTrigger - getCurrentComboSize())
            if (counter >= require) {
                counter = 0
                addToTop {
                    AbstractDungeon.player.apply {
                        applyStartOfCombatPreDrawLogic()
                        applyStartOfCombatLogic()
                        applyPreCombatLogic()
                        PatchEffect.onPostBattleStartSubscribers.forEach {
                            it.effect()
                        }
                    }
                }
                flash()
            }
        })
    }

}