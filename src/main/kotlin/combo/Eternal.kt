package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class Eternal :
    AbstractRelicCombo(
        Eternal::class.makeId(),
        hashSetOf(
            MercuryHourglass.ID,
            Sundial.ID,
            StoneCalendar.ID,
            UnceasingTop.ID,
            IncenseBurner.ID,
            RunicPyramid.ID,
            Pocketwatch.ID
        ), numberToActive = 4
    ) {
}