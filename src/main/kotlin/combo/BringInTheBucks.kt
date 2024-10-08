package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.rewards.RewardItem
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.makeId
import utils.toLog
import kotlin.math.ceil
import kotlin.math.max

class BringInTheBucks : AbstractRelicCombo(
    BringInTheBucks::class.makeId(),
    hashSetOf(CeramicFish.ID, MawBank.ID, OldCoin.ID, Abacus.ID, GoldenIdol.ID, BloodyIdol.ID),
) {
    private val maxCount = setConfigurableProperty("C", 5, ConfigurableType.Int).toInt()
    private var count = maxCount
    private var increaseHP = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()
    private var increaseGold = setConfigurableProperty("M2", 5, ConfigurableType.Int).toInt()
    override fun onActive() {
        PatchEffect.onPostEndBattleSubscribers.add(ComboEffect(caller = this) {
            repeat(getExtraCollectCount() + 1) {
                val g = AbstractDungeon.miscRng.random(5, 10)
                AbstractDungeon.effectsQueue.add(EmptyEffect {
                    AbstractDungeon.getCurrRoom().rewards.add(RewardItem(g))
                })
                flash()
            }
        })
        PatchEffect.onPreGainGoldSubscribers.add(ComboEffect(caller = this) { goldAmount ->
            flash()
            "amount:${goldAmount} combo size:${getCurrentComboSize()} extra:${getExtraCollectCount()}".toLog()
            val a: Int = goldAmount + max(0, ceil(goldAmount * getCurrentComboSize() * 0.01F * increaseGold).toInt())
            if (getExtraCollectCount() > 0 && count > 0) {
                count--
                AbstractDungeon.effectsQueue.add(EmptyEffect {
                    AbstractDungeon.player.increaseMaxHp(getExtraCollectCount() * increaseHP, true)
                })
            }
            a
        })
        PatchEffect.onPostGoNextRoomSubscribers.add(ComboEffect(caller = this) {
            count = maxCount
        })
    }
}

