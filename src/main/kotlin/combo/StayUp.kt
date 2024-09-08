package combo

import core.AbstractRelicCombo
import action.EmptyEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.rooms.RestRoom
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption
import com.megacrit.cardcrawl.ui.campfire.RestOption
import utils.makeId
import kotlin.math.max
// TODO
class StayUp : AbstractRelicCombo(
    StayUp::class.makeId(), hashSetOf(
        BagOfPreparation.ID, InkBottle.ID, UnceasingTop.ID, Shovel.ID, PenNib.ID,
        CoffeeDripper.ID, Girya.ID
    ), numberToActive = 4
) {

    var count: Int = 1
    override fun onActive() {
        TODO("Not yet implemented")
    }
}