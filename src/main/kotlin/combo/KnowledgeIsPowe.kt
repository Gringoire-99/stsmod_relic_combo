package combo

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.relics.ArtOfWar
import com.megacrit.cardcrawl.relics.Enchiridion
import com.megacrit.cardcrawl.relics.Necronomicon
import com.megacrit.cardcrawl.relics.NilrysCodex
import utils.addToQueue
import utils.makeId

class KnowledgeIsPower :
    AbstractRelicCombo(
        KnowledgeIsPower::class.makeId(),
        hashSetOf(Enchiridion.ID, Necronomicon.ID, NilrysCodex.ID, ArtOfWar.ID)
    ) {
    override fun onUseCard(c: AbstractCard?, monster: AbstractMonster?, energyOnUse: Int, combo: HashSet<String>) {
        if (c is AbstractCard && c.type == AbstractCard.CardType.ATTACK && !c.purgeOnUse) {
            addToQueue(c, monster, random = false, purge = true)
        }
    }
}