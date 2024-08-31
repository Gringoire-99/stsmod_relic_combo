package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class Library : AbstractRelicCombo(
    Library::class.makeId(),
    hashSetOf(NlothsGift.ID, DollysMirror.ID, Orrery.ID, PrayerWheel.ID, QuestionCard.ID, CeramicFish.ID)
) {
}