package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.LoseStrengthPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import utils.addToBot
import utils.isInCombat
import utils.makeId

class PainIsPower : AbstractRelicCombo(
    PainIsPower::class.makeId(),
    hashSetOf(MarkOfPain.ID, RunicCube.ID, MutagenicStrength.ID, MarkOfTheBloom.ID, SelfFormingClay.ID, RedSkull.ID)
) {
    override fun onPlayerTakingDamageFinal(damage: IntArray, combo: HashSet<String>) {
        if (isInCombat() && damage[0] > 0) {
            addToBot(
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    StrengthPower(AbstractDungeon.player, 1)
                ),
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    LoseStrengthPower(AbstractDungeon.player, 1)
                )
            )
            showText()
        }
    }
}