package combo

import core.AbstractRelicCombo
import com.megacrit.cardcrawl.relics.*
import utils.makeId

// TODO
class StayUp : AbstractRelicCombo(
    StayUp::class.makeId(), hashSetOf(
        BagOfPreparation.ID, InkBottle.ID, UnceasingTop.ID, Shovel.ID, PenNib.ID,
        CoffeeDripper.ID, Girya.ID
    ), numberToActiveCombo = 4
) {

    var count: Int = 1
    override fun onActive() {
        TODO("Not yet implemented")
    }
}