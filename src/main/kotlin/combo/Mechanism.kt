package combo

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.dealDamage
import utils.gainBlock
import utils.makeId

class Mechanism : AbstractRelicCombo(
    Mechanism::class.makeId(),
    hashSetOf(Boot.ID, Calipers.ID, Pocketwatch.ID, ClockworkSouvenir.ID, HandDrill.ID, TinyHouse.ID)
) {
    private val initialDamage = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()
    private val initialBlock = setConfigurableProperty("M2", 2, ConfigurableType.Int).toInt()
    private val repeat = setConfigurableProperty("M3", 1, ConfigurableType.Int).toInt()
    private var damage: Int = initialDamage
    private var block: Int = initialBlock

    override fun onActive() {

        PatchEffect.onPostBattleStartCleanupSubscribers.add(ComboEffect {
            damage = initialDamage
            block = initialBlock
        })
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect {
            flash()
            repeat(repeat + getExtraCollectCount()) {
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
        })
    }
}