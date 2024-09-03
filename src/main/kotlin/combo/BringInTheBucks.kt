package combo

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.rooms.AbstractRoom
import utils.addToTop
import utils.makeId
import kotlin.math.ceil
import kotlin.math.max

class BringInTheBucks : AbstractRelicCombo(
    BringInTheBucks::class.makeId(),
    hashSetOf(CeramicFish.ID, MawBank.ID, OldCoin.ID, Abacus.ID, GoldenIdol.ID, BloodyIdol.ID),
) {
    override fun onEndBattle(room: AbstractRoom, combo: HashSet<String>) {
        repeat(max(0, combo.size - numberToActive + 1)) {
            val g = AbstractDungeon.miscRng.random(5, 10)
            addToTop {
                room.rewards.add(RewardItem(g))
            }
            showText()
        }
    }

    override fun onGainGold(amount: IntArray, combo: HashSet<String>) {
        amount[0] = amount[0] + max(0, ceil(amount[0] * combo.size * 0.05).toInt())
        repeat(max(0, combo.size - numberToActive)) {
            AbstractDungeon.player.increaseMaxHp(1, true)
        }
        showText()
    }
}

