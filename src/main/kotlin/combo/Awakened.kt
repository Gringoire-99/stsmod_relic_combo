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
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.addToTop
import utils.isInCombat
import utils.makeId

class Awakened : AbstractRelicCombo(
    Awakened::class.makeId(),
    hashSetOf(EternalFeather.ID, EmptyCage.ID, CultistMask.ID, BirdFacedUrn.ID, Torii.ID)
) {
    private var isUsed: Boolean = false

    private val magic = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()
    private val heal = setConfigurableProperty("M2", 4, ConfigurableType.Int).toInt()

    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect {
            isUsed = false
            addToTop(
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    RitualPower(AbstractDungeon.player, magic, true)
                )
            )
            flash()
        })
        PatchEffect.onPostPlayerTakingDamageSubscribers.add(ComboEffect(priority = Int.MIN_VALUE) { damage: Int, _ ->
            var d = damage
            if (d >= AbstractDungeon.player.currentHealth && !isUsed && isInCombat()) {
                d = 0
                isUsed = true
                addToTop(
                    ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        StrengthPower(AbstractDungeon.player, magic * getCurrentComboSize())
                    ),
                    HealAction(AbstractDungeon.player, AbstractDungeon.player, heal * getCurrentComboSize()),
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
                flash()
            }
            d
        })
    }
}