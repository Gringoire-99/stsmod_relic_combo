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
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.addToBot
import utils.isAlive
import utils.makeId

class ToxinPurification : AbstractRelicCombo(
    ToxinPurification::class.makeId(),
    hashSetOf(TwistedFunnel.ID, TheSpecimen.ID, SneckoSkull.ID, ChemicalX.ID, RingOfTheSerpent.ID)
) {
    private var count: Int = 0
    private val limit = setConfigurableProperty("M", 3, ConfigurableType.Int).toInt()
    private val repeat = setConfigurableProperty("M2", 1, ConfigurableType.Int).toInt()
    override fun onActive() {
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect {
            count = 0
        })
        PatchEffect.onPreApplyPowerSubscribers.add(ComboEffect { target: AbstractCreature?,
                                                                 source: AbstractCreature?,
                                                                 power: AbstractPower? ->

            if (power is PoisonPower && source is AbstractPlayer && target is AbstractMonster) {
                count++
                if (count == limit) {
                    flash()
                    count = 0
                    addToBot {
                        AbstractDungeon.getMonsters().monsters.forEach {
                            val power1 = it.getPower(PoisonPower.POWER_ID)
                            if (it.isAlive() && power1 != null && power1.amount > 0) {
                                repeat(repeat + getExtraCollectCount()) {
                                    addToBot(
                                        PoisonLoseHpAction(
                                            power1.owner,
                                            source,
                                            power1.amount,
                                            AbstractGameAction.AttackEffect.POISON
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            true

        })
    }

}