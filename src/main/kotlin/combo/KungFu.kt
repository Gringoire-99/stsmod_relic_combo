package combo

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.purple.PressurePoints
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.relics.ArtOfWar
import com.megacrit.cardcrawl.relics.Duality
import com.megacrit.cardcrawl.relics.Nunchaku
import com.megacrit.cardcrawl.relics.StrikeDummy
import utils.*

class KungFu :
    AbstractRelicCombo(KungFu::class.makeId(), hashSetOf(ArtOfWar.ID, Nunchaku.ID, StrikeDummy.ID, Duality.ID)) {
    override fun onBattleStart(combo: HashSet<String>) {
        showText()
        addToTop {
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
    }

    override fun onUseCard(c: AbstractCard?, monster: AbstractMonster?, energyOnUse: Int, combo: HashSet<String>) {
        if (c is PressurePoints && !c.purgeOnUse) {
            addToQueue(c, monster, random = false, purge = true)
            showText()
        }
    }
}