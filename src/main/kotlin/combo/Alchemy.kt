package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.drawCard
import utils.isInCombat
import utils.makeId
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
    override fun onActive() {
        PatchEffect.onPreDropRandomPotionSubscribers.add(ComboEffect { c ->
            min(99, c + 10 * getCurrentComboSize())
        })
        PatchEffect.onPostUsePotionSubscribers.add(ComboEffect { p, t ->
            if (isInCombat()) {
                if (getExtraCollectCount() > 0) {
                    drawCard(getExtraCollectCount())
                }
            }
            AbstractDungeon.effectsQueue.add(EmptyEffect {
                repeat(1 + getExtraCollectCount()) {
                    p.use(t)
                }
            })
            flash()
        })
    }
}