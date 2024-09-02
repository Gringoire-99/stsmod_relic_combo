package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardType
import com.megacrit.cardcrawl.cards.tempCards.Shiv
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AccuracyPower
import com.megacrit.cardcrawl.relics.*
import utils.addToBot
import utils.makeId
import kotlin.math.max

class Ninja :
    AbstractRelicCombo(
        Ninja::class.makeId(),
        hashSetOf(Kunai.ID, Shuriken.ID, NinjaScroll.ID, WristBlade.ID, OrnamentalFan.ID)
    ) {
    private var maxTriggerCount = 1
    private var triggerCount = 0
    private var attackCount = 0
    override fun onBattleStartCleanup(combo: HashSet<String>) {
        maxTriggerCount = 1 + max(0, combo.size - numberToActive)
    }

    override fun onStartOfTurn(combo: HashSet<String>) {
        triggerCount = 0
        attackCount = 0
    }

    override fun onUseCard(c: AbstractCard?, monster: AbstractMonster?, energyOnUse: Int, combo: HashSet<String>) {
        if (c is AbstractCard && c.type == CardType.ATTACK && triggerCount < maxTriggerCount) {
            attackCount++
            if (attackCount == 3) {
                attackCount = 0
                triggerCount++
                addToBot(
                    MakeTempCardInHandAction(Shiv().apply { upgrade() }, 1),
                    ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        AccuracyPower(AbstractDungeon.player, 2)
                    )
                )
                showText()
            }
        }
    }
}