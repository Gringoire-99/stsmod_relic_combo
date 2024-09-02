package combo

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import utils.dealDamage
import utils.gainBlock
import utils.makeId
import kotlin.math.max

class Mechanism : AbstractRelicCombo(
    Mechanism::class.makeId(),
    hashSetOf(Boot.ID, Calipers.ID, Pocketwatch.ID, ClockworkSouvenir.ID, HandDrill.ID)
) {
    val initialDamage = 1
    val initialBlock = 2
    var damage: Int = initialDamage
    var block: Int = initialBlock

    override fun onBattleStartCleanup(combo: HashSet<String>) {
        damage = initialDamage
        block = initialBlock
    }

    override fun onStartOfTurn(combo: HashSet<String>) {
        showText()
        repeat(1 + max(0, combo.size - numberToActive)) {
            dealDamage(
                p = AbstractDungeon.player,
                m = null,
                damage = damage,
                DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS),
                damageEffect = AbstractGameAction.AttackEffect.BLUNT_LIGHT
            )
            gainBlock(AbstractDungeon.player, block)
            damage++
        }
    }
}