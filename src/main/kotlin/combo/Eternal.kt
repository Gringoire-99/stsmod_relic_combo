package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
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
    override fun onBattleStart(combo: HashSet<String>) {
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
        showText()
    }

    override fun onBattleStartCleanup(combo: HashSet<String>) {
        counter = 0
    }

    override fun onStartOfTurn(combo: HashSet<String>) {
        counter += 1
        val require = max(1, numberToTrigger - combo.size)
        if (counter >= require) {
            counter = 0
            addToTop {
                AbstractDungeon.player.apply {
                    applyStartOfCombatPreDrawLogic()
                    applyStartOfCombatLogic()
                    currentComboSet.forEach { (k, v) ->
                        if (k.isActive(v)) {
                            k.onBattleStart(v)
                        }
                    }
                }
            }
            showText()
        }
    }
}