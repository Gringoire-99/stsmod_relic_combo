package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.rooms.RestRoom
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption
import com.megacrit.cardcrawl.ui.campfire.RestOption
import utils.makeId
import utils.toLog
import kotlin.math.max
// TODO
class StayUp : AbstractRelicCombo(
    StayUp::class.makeId(), hashSetOf(
        BagOfPreparation.ID, InkBottle.ID, UnceasingTop.ID, Shovel.ID, PenNib.ID,
        CoffeeDripper.ID, Girya.ID
    ), numberToActive = 4
) {

    var count: Int = 1

    override fun onNextRoom(combo: HashSet<String>) {
        if (AbstractDungeon.getCurrRoom() !is RestRoom) {
            count = 1 + max(0, combo.size - numberToActive)
        }
    }

    override fun onInitCampfireUI(buttons: ArrayList<AbstractCampfireOption>, combo: HashSet<String>) {
        buttons.find { it is RestOption }?.usable = false
    }

    override fun onUseCampfireOption(option: AbstractCampfireOption, combo: HashSet<String>){
        if (count > 0) {
            count--
            val currRoom = AbstractDungeon.getCurrRoom()
            if (currRoom is RestRoom) {
                AbstractDungeon.effectList.add(EmptyEffect {
                })
            }
        }
    }
}