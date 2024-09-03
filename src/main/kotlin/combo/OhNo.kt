package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import utils.makeId

class OhNo : AbstractRelicCombo(
    OhNo::class.makeId(),
    hashSetOf(NlothsMask.ID, MarkOfTheBloom.ID, SpiritPoop.ID, GremlinMask.ID),
    numberToActive = 2
) {
    override fun onObtainRelic(r: AbstractRelic, combo: HashSet<String>) {
        AbstractDungeon.effectList.add(EmptyEffect {
            combo.forEach { c -> AbstractDungeon.player.relics.removeIf { it.relicId == c } }
        })
        showText()
    }
}