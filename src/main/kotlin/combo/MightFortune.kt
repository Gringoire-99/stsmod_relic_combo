package combo

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
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
    private val m = setConfigurableProperty("M", 20, ConfigurableType.Int).toInt()

    override fun onActive() {

        PatchEffect.onPreRollEventSubscribers.add(ComboEffect {
                forceChest: BooleanArray,
                _: IntArray,
                _: IntArray,
                _: IntArray,
                _: IntArray,
            ->
            val randomBoolean = AbstractDungeon.miscRng.randomBoolean(getCurrentComboSize() * 0.01F * m)
            if (randomBoolean) {
                forceChest[0] = true
                flash()
            }
        })
        PatchEffect.onPostOpenChestSubscribers.add(ComboEffect {
            val randomBoolean = AbstractDungeon.miscRng.randomBoolean(getCurrentComboSize() * 0.01F * m)
            if (randomBoolean && AbstractDungeon.currMapNode != null) {
                flash()
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier())
            }
        })
    }
}