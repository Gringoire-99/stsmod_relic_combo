package combo

import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.stances.CalmStance
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.drawCard
import utils.makeId

class DeepMeditation : AbstractRelicCombo(
    DeepMeditation::class.makeId(),
    hashSetOf(JuzuBracelet.ID, TeardropLocket.ID, HolyWater.ID, IncenseBurner.ID, PeacePipe.ID, VioletLotus.ID)
) {
    private val magic = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()

    override fun onActive() {
        PatchEffect.onPostChangeStanceSubscribers.add(ComboEffect(caller = this) { oldStance: AbstractStance?, newStance: AbstractStance? ->
            if ((newStance is CalmStance && oldStance !is CalmStance) || (oldStance is CalmStance && newStance !is CalmStance)) {
                drawCard(magic)
                flash()
            }
        })
    }
}