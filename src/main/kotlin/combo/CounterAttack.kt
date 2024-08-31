package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class CounterAttack : AbstractRelicCombo(
    CounterAttack::class.makeId(),
    hashSetOf(
        BronzeScales.ID,
        LetterOpener.ID,
        EmotionChip.ID,
        TungstenRod.ID,
        Torii.ID,
        ToughBandages.ID,
        ThreadAndNeedle.ID, FossilizedHelix.ID
    )
) {
}