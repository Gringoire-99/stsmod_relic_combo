package combo

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.purple.PressurePoints
import com.megacrit.cardcrawl.relics.ArtOfWar
import com.megacrit.cardcrawl.relics.Duality
import com.megacrit.cardcrawl.relics.Nunchaku
import com.megacrit.cardcrawl.relics.StrikeDummy
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.*

class KungFu :
    AbstractRelicCombo(KungFu::class.makeId(), hashSetOf(ArtOfWar.ID, Nunchaku.ID, StrikeDummy.ID, Duality.ID)) {
    private val damage = setConfigurableProperty("M", 4, ConfigurableType.Int).toInt()
    private val reduceCost = setConfigurableProperty("M2", 1, ConfigurableType.Int).toInt()
    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect {
            flash()
            addToBot {
                getAllGroup().forEach {
                    it.group.forEach { c ->
                        if (c.hasTag(AbstractCard.CardTags.STRIKE)) {
                            c.modifyCostForCombat(-reduceCost)
                            c.upDamage(damage)
                        }
                        if (c is PressurePoints) {
                            c.modifyCostForCombat(-reduceCost)
                            c.upMagicNumber(damage)
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