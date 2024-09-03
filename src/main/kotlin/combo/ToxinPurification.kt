package combo

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.powers.PoisonPower
import com.megacrit.cardcrawl.relics.*
import utils.addToBot
import utils.isAlive
import utils.makeId
import kotlin.math.max

class ToxinPurification : AbstractRelicCombo(
    ToxinPurification::class.makeId(),
    hashSetOf(TwistedFunnel.ID, TheSpecimen.ID, SneckoSkull.ID, ChemicalX.ID, RingOfTheSerpent.ID)
) {
    private var count: Int = 0

    override fun onStartOfTurn(combo: HashSet<String>) {
        count = 0
    }

    override fun onApplyPower(
        combo: HashSet<String>,
        target: AbstractCreature?,
        source: AbstractCreature?,
        power: AbstractPower?
    ): Boolean {
        if (power is PoisonPower && source is AbstractPlayer && target is AbstractMonster) {
            count++
            if (count == 3) {
                showText()
                count = 0
                addToBot {
                    AbstractDungeon.getMonsters().monsters.forEach {
                        val power1 = it.getPower(PoisonPower.POWER_ID)
                        if (it.isAlive() && power1 != null && power1.amount > 0) {
                            repeat(1 + max(0, combo.size - numberToActive)) {
                                addToBot(
                                    PoisonLoseHpAction(
                                        power1.owner,
                                        source,
                                        power.amount,
                                        AbstractGameAction.AttackEffect.POISON
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        return true
    }
}