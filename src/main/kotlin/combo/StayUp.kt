package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class StayUp : AbstractRelicCombo(
    StayUp::class.makeId(), hashSetOf(
        BagOfPreparation.ID, InkBottle.ID, UnceasingTop.ID, Shovel.ID, PenNib.ID,
         CoffeeDripper.ID, Girya.ID
    )
) {
}