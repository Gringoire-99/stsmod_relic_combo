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
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

/**
 *
 */
class Sacrifice : AbstractRelicCombo(
    Sacrifice::class.makeId(),
    hashSetOf(MarkOfPain.ID, RunicCube.ID, MutagenicStrength.ID, MarkOfTheBloom.ID, SelfFormingClay.ID, RedSkull.ID)
) {
    private val m = setConfigurableProperty("M", 2, ConfigurableType.Int).toInt()

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
        PatchEffect.onPreMonsterTakingDamageSubscribers.add(ComboEffect(priority = 0) { damage, _ ->
            var d = damage

            d += max(
                0,
                ceil(
                    damage * min(
                        1f, max(
                            0f,
                            1 - AbstractDungeon.player.currentHealth.toFloat() / AbstractDungeon.player.maxHealth
                        )
                    )
                ).toInt()
            )
            flash()
            d
        })
    }
}