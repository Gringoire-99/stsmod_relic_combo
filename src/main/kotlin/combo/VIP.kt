package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class VIP : AbstractRelicCombo(
    VIP::class.makeId(),
    hashSetOf(MealTicket.ID, SmilingMask.ID, OldCoin.ID, MembershipCard.ID, Courier.ID)
) {
}