package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.LoseStrengthPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.addToBot
import utils.isInCombat
import utils.makeId

/**
 *
 */
class Sacrifice : AbstractRelicCombo(
    Sacrifice::class.makeId(),
    hashSetOf(MarkOfPain.ID, RunicCube.ID, MutagenicStrength.ID, MarkOfTheBloom.ID, SelfFormingClay.ID, RedSkull.ID)
) {
    private val m = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()

    override fun onActive() {
        PatchEffect.onPrePlayerLoseHpSubscribers.add(ComboEffect { damage, t ->
            val d = damage
            if (isInCombat() && damage > 0) {
                addToBot(
                    ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        StrengthPower(AbstractDungeon.player, m)
                    ),
                    ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        LoseStrengthPower(AbstractDungeon.player, m)
                    )
                )
                flash()
            }
            d
        })
    }
}