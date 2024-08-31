package combo

import com.megacrit.cardcrawl.relics.GremlinMask
import com.megacrit.cardcrawl.relics.MarkOfTheBloom
import com.megacrit.cardcrawl.relics.NlothsMask
import com.megacrit.cardcrawl.relics.SpiritPoop
import utils.makeId

class OhNo : AbstractRelicCombo(
    OhNo::class.makeId(),
    hashSetOf(NlothsMask.ID, MarkOfTheBloom.ID, SpiritPoop.ID, GremlinMask.ID),
    numberToActive = 2
) {
}