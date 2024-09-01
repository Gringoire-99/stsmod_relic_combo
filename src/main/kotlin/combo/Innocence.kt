package combo

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.stances.DivinityStance
import utils.addToTop
import utils.isInCombat
import utils.makeId
import kotlin.math.max

class Innocence : AbstractRelicCombo(
    Innocence::class.makeId(),
    combo = hashSetOf(
        Akabeko.ID,
        BagOfMarbles.ID,
        HappyFlower.ID,
        ToyOrnithopter.ID,
        TinyChest.ID,
        Matryoshka.ID,
        Damaru.ID,
        HoveringKite.ID,
        PaperCrane.ID,
        PaperFrog.ID,
    ), numberToActive = 2
) {
    override fun onStartOfTurn(combo: HashSet<String>) {
        if (combo.size == this.combo.size) {
            addToTop(ChangeStanceAction(DivinityStance.STANCE_ID))
        }
    }

    override fun onPlayerTakingDamageFinal(damage: IntArray, combo: HashSet<String>) {
        if (!isInCombat() || combo.size == this.combo.size) {
            damage[0] = max(0, damage[0] - combo.size * 3)
        }
    }
}