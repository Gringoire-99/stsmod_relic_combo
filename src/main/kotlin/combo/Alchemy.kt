package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.PotionHelper
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.drawCard
import utils.isInCombat
import utils.makeId

class Alchemy : AbstractRelicCombo(
    Alchemy::class.makeId(),
    hashSetOf(
        PhilosopherStone.ID,
        Brimstone.ID,
        Cauldron.ID,
        ChemicalX.ID,
        PotionBelt.ID,
        WhiteBeast.ID,
        SacredBark.ID
    ),
) {
    private val chance: Int = setConfigurableProperty("C", 1, ConfigurableType.Int).toInt()
    private val magic: Int = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()

    override fun onActive() {
        PatchEffect.onPreDropRandomPotionSubscribers.add(ComboEffect(caller = this) { c ->
            var result = c
            if (AbstractDungeon.miscRng.randomBoolean(
                    getChance(
                        Fraction(
                            dividend = getCurrentComboSize() * chance.toFloat(),
                            divisor = 7f
                        )
                    )
                )
            ) {
                if (result < PotionHelper.POTION_COMMON_CHANCE) {
                    result = PotionHelper.POTION_COMMON_CHANCE
                    flash()
                } else if (result < PotionHelper.POTION_COMMON_CHANCE + PotionHelper.POTION_UNCOMMON_CHANCE) {
                    result = PotionHelper.POTION_COMMON_CHANCE + PotionHelper.POTION_UNCOMMON_CHANCE
                    flash()
                }
            }
            result
        })
        PatchEffect.onPostUsePotionSubscribers.add(ComboEffect(caller = this) { p, t ->
            if (isInCombat()) {
                if (getExtraCollectCount() > 0) {
                    drawCard(getExtraCollectCount() * magic)
                }
            }
            if (AbstractDungeon.miscRng.randomBoolean(
                    getChance(
                        Fraction(
                            dividend = getCurrentComboSize() * chance.toFloat(),
                            divisor = 10f
                        )
                    )
                )
            ) {
                AbstractDungeon.effectsQueue.add(EmptyEffect {
                    AbstractDungeon.player.obtainPotion(AbstractDungeon.returnRandomPotion())
                })
                flash()
            }
        })
    }
}
