package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.defect.ImpulseAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.FocusPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import utils.addToBot
import utils.addToQueue
import utils.makeId
import kotlin.math.max

class Repair : AbstractRelicCombo(
    Repair::class.makeId(),
    hashSetOf(
        Inserter.ID,
        FrozenCore.ID,
        NuclearBattery.ID,
        RunicCapacitor.ID,
        EmotionChip.ID,
        GoldPlatedCables.ID,
        DataDisk.ID
    ),
) {
    override fun onBattleStart(combo: HashSet<String>) {
        showText()
        repeat(1 + max(0, combo.size - numberToActive)) {
            addToBot(
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    StrengthPower(AbstractDungeon.player, 1)
                ),
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    DexterityPower(AbstractDungeon.player, 1)
                ),
                ApplyPowerAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    FocusPower(AbstractDungeon.player, 1)
                ),
            )
        }
    }

    override fun onStartOfTurn(combo: HashSet<String>) {
        showText()
        val c = 1 + max(0, combo.size - numberToActive)
        repeat(c) {
            addToBot(
                ImpulseAction(),
            )
        }
    }

    override fun onUseCard(c: AbstractCard?, monster: AbstractMonster?, energyOnUse: Int, combo: HashSet<String>) {
        if (c is AbstractCard && c.type == AbstractCard.CardType.ATTACK && !c.purgeOnUse) {
            showText()
            addToQueue(c, monster, random = false, purge = true)
        }
    }
}