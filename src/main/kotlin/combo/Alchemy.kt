package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
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
    private val rate: Int = setConfigurableProperty("Rate", 10, ConfigurableType.Int).toInt()
    private val magic: Int = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()

    override fun onActive() {
        PatchEffect.onPreDropRandomPotionSubscribers.add(ComboEffect { c ->
            min(99, c + rate * getCurrentComboSize())
        })
        PatchEffect.onPostUsePotionSubscribers.add(ComboEffect { p, t ->
            if (isInCombat()) {
                if (getExtraCollectCount() > 0) {
                    drawCard(getExtraCollectCount() * magic)
                }
            }
            AbstractDungeon.effectsQueue.add(EmptyEffect {
                repeat(magic + getExtraCollectCount()) {
                    p.use(t)
                }
            })
            flash()
        })
    }
}