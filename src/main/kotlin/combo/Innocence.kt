package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class Innocence : AbstractRelicCombo(
    Innocence::class.makeId(),
    combo = hashSetOf(
        Akabeko.ID,
        BagOfMarbles.ID,
        HappyFlower.ID,
        ToyOrnithopter.ID,
        TinyChest.ID,
        Matryoshka.ID,
        Damaru.ID,
        HoveringKite.ID,
        PaperCrane.ID,
        PaperFrog.ID,
    ), numberToActive = 2
) {
}