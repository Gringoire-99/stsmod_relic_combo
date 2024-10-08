package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.addToBot
import utils.makeId

class Yummy() : AbstractRelicCombo(
    id = Yummy::class.makeId(), combo = hashSetOf(
        Strawberry.ID, MeatOnTheBone.ID, Pear.ID, IceCream.ID, Mango.ID, OddMushroom.ID, Waffle.ID
    ), numberToActiveCombo = 2
) {
    private val m = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()
    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect(caller = this) {
            flash()
            val s = (getCurrentComboSize() / 2) * m
            val d = getCurrentComboSize() - s
            addToBot(
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    StrengthPower(AbstractDungeon.player, s),
                    s
                ), ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    DexterityPower(AbstractDungeon.player, d),
                    s
                )
            )
        })
    }
}