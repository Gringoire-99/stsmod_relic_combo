package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class Ninja :
    AbstractRelicCombo(
        Ninja::class.makeId(),
        hashSetOf(Kunai.ID, Shuriken.ID, NinjaScroll.ID, WristBlade.ID, OrnamentalFan.ID)
    ) {
}