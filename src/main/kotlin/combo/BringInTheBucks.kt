package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.rewards.RewardItem
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.makeId
import utils.toLog
import kotlin.math.ceil
import kotlin.math.max

class BringInTheBucks : AbstractRelicCombo(
    BringInTheBucks::class.makeId(),
    hashSetOf(CeramicFish.ID, MawBank.ID, OldCoin.ID, Abacus.ID, GoldenIdol.ID, BloodyIdol.ID),
) {
    var count = 5
    override fun onActive() {
        PatchEffect.onPostEndBattleSubscribers.add(ComboEffect {
            repeat(getExtraCollectCount() + 1) {
                val g = AbstractDungeon.miscRng.random(5, 10)
                AbstractDungeon.effectsQueue.add(EmptyEffect {
                    AbstractDungeon.getCurrRoom().rewards.add(RewardItem(g))
                })
                flash()
            }
        })
        PatchEffect.onPreGainGoldSubscribers.add(ComboEffect { goldAmount ->
            flash()
            "amount:${goldAmount} combo size:${getCurrentComboSize()} extra:${getExtraCollectCount()}".toLog()
            val a: Int = goldAmount + max(0, ceil(goldAmount * getCurrentComboSize() * 0.05F).toInt())
            if (getExtraCollectCount() > 0 && count > 0) {
                count--
                AbstractDungeon.effectsQueue.add(EmptyEffect {
                    AbstractDungeon.player.increaseMaxHp(getExtraCollectCount(), true)
                })
            }
            a
        })
        PatchEffect.onPostGoNextRoomSubscribers.add(ComboEffect {
            count = 5
        })
    }
}

