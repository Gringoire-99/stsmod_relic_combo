package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class BringInTheBucks : AbstractRelicCombo(
    BringInTheBucks::class.makeId(),
    hashSetOf(CeramicFish.ID, MawBank.ID, OldCoin.ID, Abacus.ID, GoldenIdol.ID, BloodyIdol.ID),
) {
}