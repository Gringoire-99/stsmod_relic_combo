package combo

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.relics.ArtOfWar
import com.megacrit.cardcrawl.relics.Enchiridion
import com.megacrit.cardcrawl.relics.Necronomicon
import com.megacrit.cardcrawl.relics.NilrysCodex
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.addToQueue
import utils.makeId

class Bookworm :
    AbstractRelicCombo(
        Bookworm::class.makeId(),
        hashSetOf(Enchiridion.ID, Necronomicon.ID, NilrysCodex.ID, ArtOfWar.ID)
    ) {

    override fun onActive() {
        PatchEffect.onPostUseCardSubscribers.add(ComboEffect { c, t ->
            if (c.type == AbstractCard.CardType.ATTACK && !c.purgeOnUse) {
                flash()
                addToQueue(c, t, random = false, purge = true)
            }
        })
    }
}