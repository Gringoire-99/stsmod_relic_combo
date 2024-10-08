package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.addToBot
import utils.makeId
import utils.toLog
import kotlin.math.max

class AncientCreation : AbstractRelicCombo(
    AncientCreation::class.makeId(),
    hashSetOf(CentennialPuzzle.ID, GoldenEye.ID, RunicCube.ID, RunicDome.ID, RunicPyramid.ID, RunicCapacitor.ID)
) {
    override fun onActive() {
        PatchEffect.onPreApplyPowerSubscribers.add(ComboEffect(caller = this) { target: AbstractCreature?,
                                                                 source: AbstractCreature?,
                                                                 power: AbstractPower? ->
            var isApply = true
            if (target is AbstractPlayer &&
                source is AbstractMonster &&
                power is AbstractPower &&
                power.type == AbstractPower.PowerType.DEBUFF
            ) {
                addToBot(
                    ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        StrengthPower(AbstractDungeon.player, max(1, power.amount)), max(1, power.amount)
                    )
                )
                flash()
                isApply = false
            }
            isApply
        })
    }
}