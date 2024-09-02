package combo

import com.megacrit.cardcrawl.relics.Anchor
import com.megacrit.cardcrawl.relics.CaptainsWheel
import com.megacrit.cardcrawl.relics.HornCleat
import utils.drawCard
import utils.makeId

class SetSail :
    AbstractRelicCombo(
        SetSail::class.makeId(),
        combo = hashSetOf(Anchor.ID, HornCleat.ID, CaptainsWheel.ID),
        numberToActive = 2
    ) {
    override fun onBattleStart(combo: HashSet<String>) {
        showText()
        drawCard(combo.size)
    }
}