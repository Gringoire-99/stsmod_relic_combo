package combo

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.stances.DivinityStance
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.addToBot
import utils.isInCombat
import utils.makeId
import kotlin.math.max

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
    ), numberToActiveCombo = 2
) {
    private val m = setConfigurableProperty("M", 3, ConfigurableType.Int).toInt()

    override fun onActive() {
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect {
            if (getCurrentComboSize() == this.combo.size) {
                flash()
                addToBot(ChangeStanceAction(DivinityStance.STANCE_ID))
            }
        })
        PatchEffect.onPrePlayerTakingDamageSubscribers.add(ComboEffect { damage, _ ->
            var d = damage
            if (!isInCombat() || getCurrentComboSize() == this.combo.size) {
                flash()
                d = max(0, d - getCurrentComboSize() * m)
            }
            d
        })
    }
}