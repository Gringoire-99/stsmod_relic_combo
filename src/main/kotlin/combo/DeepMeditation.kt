package combo

import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.stances.CalmStance
import utils.drawCard
import utils.makeId

class DeepMeditation : AbstractRelicCombo(
    DeepMeditation::class.makeId(),
    hashSetOf(JuzuBracelet.ID, TeardropLocket.ID, HolyWater.ID, IncenseBurner.ID, PeacePipe.ID, VioletLotus.ID)
) {
    override fun onChangeStance(oldStance: AbstractStance?, newStance: AbstractStance?, combo: HashSet<String>) {
        if (newStance is CalmStance || oldStance is CalmStance) {
            drawCard(1)
            showText()
        }
    }
}