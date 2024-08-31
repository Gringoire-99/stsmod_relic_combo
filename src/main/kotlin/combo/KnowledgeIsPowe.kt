package combo

import com.megacrit.cardcrawl.relics.ArtOfWar
import com.megacrit.cardcrawl.relics.Enchiridion
import com.megacrit.cardcrawl.relics.Necronomicon
import com.megacrit.cardcrawl.relics.NilrysCodex
import utils.makeId

class KnowledgeIsPower :
    AbstractRelicCombo(
        KnowledgeIsPower::class.makeId(),
        hashSetOf(Enchiridion.ID, Necronomicon.ID, NilrysCodex.ID, ArtOfWar.ID)
    )