package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class Workaholic : AbstractRelicCombo(
    Workaholic::class.makeId(), hashSetOf(
        BagOfPreparation.ID, InkBottle.ID, UnceasingTop.ID, Shovel.ID, PenNib.ID,
        Toolbox.ID, CoffeeDripper.ID, Girya.ID
    )
) {
}