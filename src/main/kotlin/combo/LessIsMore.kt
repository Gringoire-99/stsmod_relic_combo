package combo

import basemod.BaseMod
import com.megacrit.cardcrawl.actions.common.GainEnergyAction
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.*

class LessIsMore : AbstractRelicCombo(
    LessIsMore::class.makeId(),
    hashSetOf(EmptyCage.ID, CharonsAshes.ID, PeacePipe.ID, SmilingMask.ID, SingingBowl.ID, BustedCrown.ID)
) {

    private val limit = setConfigurableProperty("M", 10, ConfigurableType.Int).toInt()
    private val upgrade = setConfigurableProperty("M2", 1, ConfigurableType.Int).toInt()
    private val gain = setConfigurableProperty("M3", 1, ConfigurableType.Int).toInt()
    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect(caller = this) {
            if (AbstractDungeon.player.masterDeck.size() <= limit) {
                flash()
                addToTop {
                    getAllGroup().forEach {
                        it.group.forEach { c ->
                            c.upDamage(upgrade)
                            c.upBlock(upgrade)
                            c.upMagicNumber(upgrade)
                        }
                    }
                }
            }
        })
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect(caller = this) {
            val sumOf = getAllGroup().sumOf { it.group.size }
            if (sumOf <= limit) {
                flash()
                addToTop(ExpertiseAction(AbstractDungeon.player, BaseMod.MAX_HAND_SIZE), GainEnergyAction(gain))
            }
        })
    }
}