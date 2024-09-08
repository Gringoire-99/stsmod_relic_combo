package combo

import basemod.BaseMod
import com.megacrit.cardcrawl.actions.common.GainEnergyAction
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.*

class LessIsMore : AbstractRelicCombo(
    LessIsMore::class.makeId(),
    hashSetOf(EmptyCage.ID, CharonsAshes.ID, PeacePipe.ID, SmilingMask.ID, SingingBowl.ID, BustedCrown.ID)
) {
    private val count = 10
    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect {
            if (AbstractDungeon.player.masterDeck.size() <= count) {
                flash()
                addToTop {
                    getAllGroup().forEach { it ->
                        it.group.forEach { c ->
                            c.upDamage(1)
                            c.upBlock(1)
                            c.upMagicNumber(1)
                        }
                    }
                }
            }
        })
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect {
            val sumOf = getAllGroup().sumOf { it.group.size }
            if (sumOf <= count) {
                flash()
                addToTop(ExpertiseAction(AbstractDungeon.player, BaseMod.MAX_HAND_SIZE), GainEnergyAction(1))
            }
        })
    }
}