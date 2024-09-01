package combo

import action.EmptyAction
import com.megacrit.cardcrawl.actions.animations.VFXAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.actions.utility.SFXAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.powers.RitualPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect
import utils.addToTop
import utils.isInCombat
import utils.makeId

class Awakened : AbstractRelicCombo(
    Awakened::class.makeId(),
    hashSetOf(EternalFeather.ID, EmptyCage.ID, CultistMask.ID, BirdFacedUrn.ID, Torii.ID)
) {
    private var isUsed: Boolean = false
    override fun onBattleStart(combo: HashSet<String>) {
        isUsed = false
        addToTop(
            ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                RitualPower(AbstractDungeon.player, 1, true)
            )
        )
    }

    override fun onPlayerTakingDamageFinal(damage: IntArray, combo: HashSet<String>) {
        if (damage[0] >= AbstractDungeon.player.currentHealth && !isUsed && isInCombat()) {
            damage[0] = 0
            isUsed = true
            addToTop(
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    StrengthPower(AbstractDungeon.player, 1 * combo.size)
                ),
                HealAction(AbstractDungeon.player, AbstractDungeon.player, 4 * combo.size),
                EmptyAction {
                    AbstractDungeon.player.powers.forEach {
                        if (it.type == AbstractPower.PowerType.DEBUFF) {
                            addToTop(RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, it))
                        }
                    }
                },
                SFXAction("VO_AWAKENEDONE_1"), VFXAction(
                    AbstractDungeon.player,
                    IntenseZoomEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true),
                    0.05f,
                    true
                )
            )
        }
    }
}