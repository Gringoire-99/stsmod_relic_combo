package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.ThornsPower
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.addToTop
import utils.makeId
import kotlin.math.max
import kotlin.math.min

class CounterAttack : AbstractRelicCombo(
    CounterAttack::class.makeId(),
    hashSetOf(
        BronzeScales.ID,
        LetterOpener.ID,
        EmotionChip.ID,
        TungstenRod.ID,
        Torii.ID,
        ToughBandages.ID,
        ThreadAndNeedle.ID, FossilizedHelix.ID
    ),
    numberToActiveCombo = 3
) {
    private val magic = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()
    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect {
            addToTop(
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    ThornsPower(AbstractDungeon.player, getCurrentComboSize() * magic)
                )
            )
            flash()
        })
        PatchEffect.onPreMonsterTakingDamageSubscribers.add(ComboEffect { damage, info ->
            var d = damage
            if (info != null && info.type == DamageInfo.DamageType.THORNS) {
                d += min(d, max(0, AbstractDungeon.player.getPower(ThornsPower.POWER_ID)?.amount ?: 0))
                flash()
            }
            d
        })
    }
}