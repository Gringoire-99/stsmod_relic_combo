package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import utils.addToBot
import utils.makeId

class Yummy() : AbstractRelicCombo(
    id = Yummy::class.makeId(), combo = hashSetOf(
        Strawberry.ID, MeatOnTheBone.ID, Pear.ID, IceCream.ID, Mango.ID, OddMushroom.ID, Waffle.ID
    ), numberToActive = 2
) {
    override fun onBattleStart(combo: HashSet<String>) {
        showText()
        val s = combo.size / 2
        val d = combo.size - s
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
    }
}