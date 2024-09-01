package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class Mechanism : AbstractRelicCombo(
    Mechanism::class.makeId(),
    hashSetOf(Boot.ID, Calipers.ID, Pocketwatch.ID, ClockworkSouvenir.ID, HandDrill.ID)
) {
}