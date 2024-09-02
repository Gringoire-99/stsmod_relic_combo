package combo

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.relics.*
import utils.*

class Plain :
    AbstractRelicCombo(
        Plain::class.makeId(),
        hashSetOf(OddlySmoothStone.ID, Vajra.ID, TinyHouse.ID, Circlet.ID, WarPaint.ID, Whetstone.ID)
    ) {
    override fun onBattleStart(combo: HashSet<String>) {
        showText()
        addToTop {
            getAllGroup().forEach {
                it.group.forEach { c ->
                    if (c.rarity == AbstractCard.CardRarity.BASIC) {
                        if (c.baseDamage > 0) {
                            c.upDamage(c.baseDamage)
                        }
                        if (c.baseBlock > 0) {
                            c.upBlock(c.baseBlock)
                        }
                    }
                }
            }
        }
    }
}