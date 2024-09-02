package combo

import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.rooms.AbstractRoom
import utils.makeId
import kotlin.math.max

class Library : AbstractRelicCombo(
    Library::class.makeId(),
    hashSetOf(NlothsGift.ID, DollysMirror.ID, Orrery.ID, PrayerWheel.ID, QuestionCard.ID, CeramicFish.ID)
) {
    override fun onEndBattle(room: AbstractRoom, combo: HashSet<String>) {
        showText()
        repeat(1 + max(0, combo.size - numberToActive)) {
            room.rewards.add(RewardItem())
        }
    }
}