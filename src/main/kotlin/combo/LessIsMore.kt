package combo

import com.megacrit.cardcrawl.actions.common.GainEnergyAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import utils.*

class LessIsMore : AbstractRelicCombo(
    LessIsMore::class.makeId(),
    hashSetOf(EmptyCage.ID, CharonsAshes.ID, PeacePipe.ID, SmilingMask.ID, SingingBowl.ID, BustedCrown.ID)
) {
    val count = 10
    override fun onBattleStart(combo: HashSet<String>) {
        if (AbstractDungeon.player.masterDeck.size() <= count) {
            showText()
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
    }

    override fun onStartOfTurn(combo: HashSet<String>) {
        val sumOf = getAllGroup().sumOf { it.group.size }
        if (sumOf <= count) {
            showText()
            addToTop(GainEnergyAction(1))
        }
    }
}