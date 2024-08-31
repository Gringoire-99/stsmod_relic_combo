package combo

import com.megacrit.cardcrawl.relics.BustedCrown
import com.megacrit.cardcrawl.relics.Ectoplasm
import com.megacrit.cardcrawl.relics.Sozu
import utils.makeId

class TheThreeOfUs :
    AbstractRelicCombo(TheThreeOfUs::class.makeId(), hashSetOf(BustedCrown.ID, Sozu.ID, Ectoplasm.ID)) {
}