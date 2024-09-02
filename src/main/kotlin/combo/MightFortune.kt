package combo

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.rewards.chests.AbstractChest
import utils.makeId

class MightFortune : AbstractRelicCombo(
    MightFortune::class.makeId(),
    hashSetOf(
        JuzuBracelet.ID,
        TinyChest.ID,
        Matryoshka.ID,
        PandorasBox.ID,
        GamblingChip.ID,
        SneckoEye.ID,
        SsserpentHead.ID
    )
) {
    override fun onRollEvent(
        forceChest: BooleanArray,
        eliteSize: IntArray,
        monsterSize: IntArray,
        shopSize: IntArray,
        treasureSize: IntArray,
        combo: HashSet<String>
    ) {
        val randomBoolean = AbstractDungeon.miscRng.randomBoolean(combo.size * 0.2F)
        if (randomBoolean) {
            forceChest[0] = true
            showText()
        }
    }

    override fun onOpenChest(c: AbstractChest, combo: HashSet<String>) {
        val randomBoolean = AbstractDungeon.miscRng.randomBoolean(combo.size * 0.2F)
        if (randomBoolean && AbstractDungeon.currMapNode != null) {
            showText()
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier())
        }
    }
}