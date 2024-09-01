package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class Repair : AbstractRelicCombo(
    Repair::class.makeId(),
    hashSetOf(
        Inserter.ID,
        FrozenCore.ID,
        NuclearBattery.ID,
        RunicCapacitor.ID,
        EmotionChip.ID,
        GoldPlatedCables.ID,
        DataDisk.ID
    ),
) {
}