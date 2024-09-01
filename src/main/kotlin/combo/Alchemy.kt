package combo

import com.megacrit.cardcrawl.relics.*
import utils.drawCard
import utils.makeId
import kotlin.math.max
import kotlin.math.min

class Alchemy : AbstractRelicCombo(
    Alchemy::class.makeId(),
    hashSetOf(
        PhilosopherStone.ID,
        Brimstone.ID,
        Cauldron.ID,
        ChemicalX.ID,
        PotionBelt.ID,
        WhiteBeast.ID,
        SacredBark.ID
    ),
) {
    override fun onDropRandomPotion(chance: IntArray, combo: HashSet<String>) {
        chance[0] = min(99, chance[0] + 5 * combo.size)
    }

    override fun onAfterUsePotion(combo: HashSet<String>) {
        drawCard(max(0, combo.size - numberToActive))
    }
}