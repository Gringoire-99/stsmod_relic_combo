package combo

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.relics.ArtOfWar
import com.megacrit.cardcrawl.relics.Duality
import com.megacrit.cardcrawl.relics.Nunchaku
import com.megacrit.cardcrawl.relics.StrikeDummy
import utils.addToTop
import utils.getAllGroup
import utils.makeId

class KungFu :
    AbstractRelicCombo(KungFu::class.makeId(), hashSetOf(ArtOfWar.ID, Nunchaku.ID, StrikeDummy.ID, Duality.ID)) {
    override fun onBattleStart(combo: HashSet<String>) {
        addToTop {
            getAllGroup().forEach {
                it.group.forEach { c ->
                    if (c.hasTag(AbstractCard.CardTags.STRIKE)) {
                        c.updateCost(-1)
                    }
                }
            }
        }
    }
}