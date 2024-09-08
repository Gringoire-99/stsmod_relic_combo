package combo

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier
import com.megacrit.cardcrawl.relics.BustedCrown
import com.megacrit.cardcrawl.relics.Ectoplasm
import com.megacrit.cardcrawl.relics.Sozu
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.makeId

class TheThreeOfUs :
    AbstractRelicCombo(
        TheThreeOfUs::class.makeId(),
        hashSetOf(BustedCrown.ID, Sozu.ID, Ectoplasm.ID),
        numberToActive = 2
    ) {
    override fun onActive() {
        PatchEffect.onPreReturnRandomRelicSubscribers.add(ComboEffect { tier: RelicTier ->
            var t = tier
            if (t == RelicTier.RARE || t == RelicTier.UNCOMMON || t == RelicTier.COMMON) {
                if (AbstractDungeon.rareRelicPool.isNotEmpty() && t != RelicTier.RARE) {
                    t = RelicTier.RARE
                    flash()
                } else if (AbstractDungeon.uncommonRelicPool.isNotEmpty() && t != RelicTier.UNCOMMON) {
                    t = RelicTier.UNCOMMON
                    flash()
                }
            }
            t
        })
        PatchEffect.onPostOpenChestSubscribers.add(ComboEffect {
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier())
        })
    }

}