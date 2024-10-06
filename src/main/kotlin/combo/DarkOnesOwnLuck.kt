package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.vfx.RainingGoldEffect
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.addTopEffect
import utils.makeId

class DarkOnesOwnLuck : AbstractRelicCombo(
    DarkOnesOwnLuck::class.makeId(),
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
    private val m = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()
    private val money = setConfigurableProperty("Money", 10, ConfigurableType.Int).toInt()
    override fun onActive() {

        PatchEffect.onPreRollEventSubscribers.add(ComboEffect {
                forceChest: BooleanArray,
                _: IntArray,
                _: IntArray,
                _: IntArray,
                _: IntArray,
            ->
            val randomBoolean =
                AbstractDungeon.miscRng.randomBoolean(
                    getChance(
                        Fraction(
                            dividend = getCurrentComboSize() * m.toFloat(),
                            divisor = 7f
                        )
                    )
                )
            if (randomBoolean) {
                forceChest[0] = true
                flash()
            }
        })
        PatchEffect.onPostOpenChestSubscribers.add(ComboEffect {
            val randomBoolean =
                AbstractDungeon.miscRng.randomBoolean(
                    getChance(
                        Fraction(
                            dividend = getCurrentComboSize() * m.toFloat(),
                            divisor = 7f
                        )
                    )
                )
            if (randomBoolean && AbstractDungeon.currMapNode != null) {
                flash()
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractDungeon.returnRandomRelicTier())
            }
        })
        PatchEffect.onGetChanceSubscribers.add(ComboEffect { f, caller ->
            if (caller !is DarkOnesOwnLuck) {
                f.dividend++
                addTopEffect(RainingGoldEffect(money), EmptyEffect { AbstractDungeon.player.gainGold(money) })
                flash()
            }
        })
    }
}