package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class LessIsMore : AbstractRelicCombo(
    LessIsMore::class.makeId(),
    hashSetOf(EmptyCage.ID, CharonsAshes.ID, PeacePipe.ID, SmilingMask.ID, SingingBowl.ID,BustedCrown.ID)
) {
}