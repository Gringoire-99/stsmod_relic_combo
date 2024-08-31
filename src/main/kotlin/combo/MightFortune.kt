package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class MightFortune : AbstractRelicCombo(
    MightFortune::class.makeId(),
    hashSetOf(JuzuBracelet.ID, TinyChest.ID, Matryoshka.ID,PandorasBox.ID, GamblingChip.ID, SneckoEye.ID, SsserpentHead.ID)
) {
}