package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class StayHealthy : AbstractRelicCombo(
    StayHealthy::class.makeId(),
    hashSetOf(FaceOfCleric.ID, OddMushroom.ID, MedicalKit.ID, OrangePellets.ID, Ginger.ID, Girya.ID, AncientTeaSet.ID)
) {
}