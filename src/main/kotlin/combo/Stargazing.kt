package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class Stargazing : AbstractRelicCombo(
    Stargazing::class.makeId(),
    hashSetOf(GoldenEye.ID, Orrery.ID, FrozenEye.ID, Astrolabe.ID, BlackStar.ID, Melange.ID)
) {
}