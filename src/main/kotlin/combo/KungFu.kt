package combo

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.purple.PressurePoints
import com.megacrit.cardcrawl.relics.ArtOfWar
import com.megacrit.cardcrawl.relics.Duality
import com.megacrit.cardcrawl.relics.Nunchaku
import com.megacrit.cardcrawl.relics.StrikeDummy
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.*

class KungFu :
    AbstractRelicCombo(KungFu::class.makeId(), hashSetOf(ArtOfWar.ID, Nunchaku.ID, StrikeDummy.ID, Duality.ID)) {

    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect {
            flash()
            addToBot{
                getAllGroup().forEach {
                    it.group.forEach { c ->
                        if (c.hasTag(AbstractCard.CardTags.STRIKE)) {
                            c.updateCost(-1)
                            c.upDamage(4)
                        }
                        if (c is PressurePoints) {
                            c.updateCost(-1)
                            c.upMagicNumber(4)
                        }
                    }
                }
            }
        })
        PatchEffect.onPostUseCardSubscribers.add(ComboEffect { c, t ->
            if (c is PressurePoints && !c.purgeOnUse) {
                addToQueue(c, t, random = false, purge = true)
                flash()
            }
        })
    }
}