package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class ToxinPurification : AbstractRelicCombo(
    ToxinPurification::class.makeId(),
    hashSetOf(TwistedFunnel.ID, TheSpecimen.ID, SneckoSkull.ID, ChemicalX.ID, RingOfTheSerpent.ID)
) {
}