package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
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
    private val max = setConfigurableProperty("M", 7, ConfigurableType.Int).toInt()
    private val m = setConfigurableProperty("M2", 1, ConfigurableType.Int).toInt()

    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect {
            addToTop(
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    StrengthPower(AbstractDungeon.player, m)
                ),
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    DexterityPower(AbstractDungeon.player, m)
                )
            )
            flash()
        })
        PatchEffect.onPostBattleStartCleanupSubscribers.add(ComboEffect {
            counter = 0
        })
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect {
            counter += 1
            val require = max(1, max - getCurrentComboSize())
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