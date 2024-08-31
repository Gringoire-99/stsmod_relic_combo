package combo

import com.megacrit.cardcrawl.relics.AncientTeaSet
import com.megacrit.cardcrawl.relics.DreamCatcher
import com.megacrit.cardcrawl.relics.RegalPillow
import com.megacrit.cardcrawl.relics.UnceasingTop
import utils.makeId

class GetSomeSleep : AbstractRelicCombo(
    GetSomeSleep::class.makeId(),
    hashSetOf(AncientTeaSet.ID, DreamCatcher.ID, RegalPillow.ID, UnceasingTop.ID, UnceasingTop.ID)
) {
}