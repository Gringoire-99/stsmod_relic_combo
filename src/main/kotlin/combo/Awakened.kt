package combo

import com.megacrit.cardcrawl.relics.BirdFacedUrn
import com.megacrit.cardcrawl.relics.CultistMask
import com.megacrit.cardcrawl.relics.EmptyCage
import com.megacrit.cardcrawl.relics.EternalFeather
import com.megacrit.cardcrawl.relics.Torii
import utils.makeId

class Awakened : AbstractRelicCombo(
    Awakened::class.makeId(),
    hashSetOf(EternalFeather.ID, EmptyCage.ID, CultistMask.ID, BirdFacedUrn.ID,Torii.ID)
) {
}