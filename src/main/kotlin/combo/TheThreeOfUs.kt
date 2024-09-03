package combo

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier
import com.megacrit.cardcrawl.relics.BustedCrown
import com.megacrit.cardcrawl.relics.Ectoplasm
import com.megacrit.cardcrawl.relics.Sozu
import com.megacrit.cardcrawl.rewards.chests.AbstractChest
import utils.makeId

class TheThreeOfUs :
    AbstractRelicCombo(
        TheThreeOfUs::class.makeId(),
        hashSetOf(BustedCrown.ID, Sozu.ID, Ectoplasm.ID),
        numberToActive = 2
    ) {
    override fun beforeReturnRandomRelic(
        tier: RelicTier, combo: HashSet<String>
    ): RelicTier {
        var t = tier
        if (t == RelicTier.RARE || t == RelicTier.UNCOMMON || t == RelicTier.COMMON) {
            if (AbstractDungeon.rareRelicPool.isNotEmpty() && t != RelicTier.RARE) {
                t = RelicTier.RARE
                showText()
            } else if (AbstractDungeon.uncommonRelicPool.isNotEmpty() && t != RelicTier.UNCOMMON) {
                t = RelicTier.UNCOMMON
                showText()
            }
        }
        return t
    }

    override fun onOpenChest(c: AbstractChest, combo: HashSet<String>) {
        showText()
        AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier())
    }
}