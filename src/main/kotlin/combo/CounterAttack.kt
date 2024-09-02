package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.ThornsPower
import com.megacrit.cardcrawl.relics.*
import utils.addToTop
import utils.makeId

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
    )
) {
    override fun onBattleStart(combo: HashSet<String>) {
        addToTop(
            ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                ThornsPower(AbstractDungeon.player, combo.size)
            )
        )
        showText()
    }

    override fun onMonsterTakingDamageStart(info: DamageInfo, damageAmount: IntArray, combo: HashSet<String>) {
        if (info.type == DamageInfo.DamageType.THORNS) {
            damageAmount[0] *= 2
        }
    }
}