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

class Library : AbstractRelicCombo(
    Library::class.makeId(),
    hashSetOf(NlothsGift.ID, DollysMirror.ID, Orrery.ID, PrayerWheel.ID, QuestionCard.ID, CeramicFish.ID)
) {
    private val magic = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()

    override fun onActive() {
        PatchEffect.onPostEndBattleSubscribers.add(ComboEffect {
            flash()
            AbstractDungeon.effectsQueue.add(EmptyEffect {
                repeat(magic + getExtraCollectCount()) {
                    AbstractDungeon.getCurrRoom().rewards.add(RewardItem())
                }
            })
        })
    }

}