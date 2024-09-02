package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import utils.addToBot
import utils.makeId
import kotlin.math.max

class AncientCreation : AbstractRelicCombo(
    AncientCreation::class.makeId(),
    hashSetOf(CentennialPuzzle.ID, GoldenEye.ID, RunicCube.ID, RunicDome.ID, RunicPyramid.ID, RunicCapacitor.ID)
) {
    override fun onApplyPower(
        combo: HashSet<String>,
        target: AbstractCreature?,
        source: AbstractCreature?,
        power: AbstractPower?
    ): Boolean {
        if (target is AbstractPlayer && source is AbstractMonster && power is AbstractPower && power.type == AbstractPower.PowerType.DEBUFF) {
            showText()
            addToBot(
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    StrengthPower(AbstractDungeon.player, max(1, power.amount)), max(1, power.amount)
                )
            )
            showText()
            return false
        }
        return true
    }
}