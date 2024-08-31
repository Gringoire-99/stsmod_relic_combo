package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class FourElement : AbstractRelicCombo(
    FourElement::class.makeId(),
    hashSetOf(
        BottledFlame.ID,
        BottledLightning.ID,
        BottledTornado.ID,
        FrozenEgg2.ID,
        MoltenEgg2.ID,
        ToxicEgg2.ID,
        PrismaticShard.ID
    ), numberToActive = 4
) {
}