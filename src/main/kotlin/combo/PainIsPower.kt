package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class PainIsPower : AbstractRelicCombo(
    PainIsPower::class.makeId(),
    hashSetOf(MarkOfPain.ID, RunicCube.ID, MutagenicStrength.ID, MarkOfTheBloom.ID, SelfFormingClay.ID, RedSkull.ID)
) {
}