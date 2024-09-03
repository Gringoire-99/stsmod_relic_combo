package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.potions.AbstractPotion
import com.megacrit.cardcrawl.relics.*
import utils.drawCard
import utils.isInCombat
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
        chance[0] = min(99, chance[0] + 10 * combo.size)
    }

    override fun onAfterUsePotion(potion: AbstractPotion, target: AbstractMonster?, combo: HashSet<String>) {
        if (isInCombat()) {
            drawCard(max(0, combo.size - numberToActive))
        }
        repeat(1 + max(0, combo.size - numberToActive)) {
            AbstractDungeon.effectsQueue.add(EmptyEffect {
                potion.use(target)
            })
        }
        showText()
    }
}