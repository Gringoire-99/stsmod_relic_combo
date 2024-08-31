package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class DeepMeditation : AbstractRelicCombo(
    DeepMeditation::class.makeId(),
    hashSetOf(JuzuBracelet.ID, TeardropLocket.ID, HolyWater.ID, IncenseBurner.ID, PeacePipe.ID, VioletLotus.ID)
) {
}