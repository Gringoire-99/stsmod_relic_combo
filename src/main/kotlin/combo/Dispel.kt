package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class Dispel : AbstractRelicCombo(
    Dispel::class.makeId(),
    hashSetOf(
        Necronomicon.ID,
        CursedKey.ID,
        CallingBell.ID,
        DuVuDoll.ID,
        BlueCandle.ID,
        DarkstonePeriapt.ID,
        Omamori.ID
    )
) {
}