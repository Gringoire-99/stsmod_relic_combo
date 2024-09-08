package combo

import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.stances.CalmStance
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.drawCard
import utils.makeId

class DeepMeditation : AbstractRelicCombo(
    DeepMeditation::class.makeId(),
    hashSetOf(JuzuBracelet.ID, TeardropLocket.ID, HolyWater.ID, IncenseBurner.ID, PeacePipe.ID, VioletLotus.ID)
) {
    override fun onActive() {
        PatchEffect.onPostChangeStanceSubscribers.add(ComboEffect { oldStance: AbstractStance?, newStance: AbstractStance? ->
            if ((newStance is CalmStance && oldStance !is CalmStance) || (oldStance is CalmStance && newStance !is CalmStance)) {
                drawCard(1)
                flash()
            }
        })
    }
}