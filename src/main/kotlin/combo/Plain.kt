package combo

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.*

class Plain :
    AbstractRelicCombo(
        Plain::class.makeId(),
        hashSetOf(OddlySmoothStone.ID, Vajra.ID, TinyHouse.ID, Circlet.ID, WarPaint.ID, Whetstone.ID)
    ) {
    private val m = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()
    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect(priority = 0) {
            flash()
            addToBot {
                getAllGroup().forEach {
                    it.group.forEach { c ->
                        if (c.rarity == AbstractCard.CardRarity.BASIC) {
                            if (c.baseDamage > 0) {
                                c.upDamage(c.baseDamage * m)
                            }
                            if (c.baseBlock > 0) {
                                c.upBlock(c.baseBlock * m)
                            }
                        }
                    }
                }
            }
        })
    }
}