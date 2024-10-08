package combo

import com.megacrit.cardcrawl.relics.Anchor
import com.megacrit.cardcrawl.relics.CaptainsWheel
import com.megacrit.cardcrawl.relics.HornCleat
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.drawCard
import utils.makeId

class SetSail :
    AbstractRelicCombo(
        SetSail::class.makeId(),
        combo = hashSetOf(Anchor.ID, HornCleat.ID, CaptainsWheel.ID),
        numberToActiveCombo = 2
    ) {
    private val m = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()
    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect(caller = this) {
            flash()
            drawCard(getCurrentComboSize() * m)
        })
    }
}