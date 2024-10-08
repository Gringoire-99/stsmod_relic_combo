package combo

import action.EmptyAction
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.green.Eviscerate
import com.megacrit.cardcrawl.cards.purple.PressurePoints
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.watcher.MarkPower
import com.megacrit.cardcrawl.relics.ArtOfWar
import com.megacrit.cardcrawl.relics.Duality
import com.megacrit.cardcrawl.relics.Nunchaku
import com.megacrit.cardcrawl.relics.StrikeDummy
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.*

class KungFu :
    AbstractRelicCombo(KungFu::class.makeId(), hashSetOf(ArtOfWar.ID, Nunchaku.ID, StrikeDummy.ID, Duality.ID)) {
    private val damage = setConfigurableProperty("M", 4, ConfigurableType.Int).toInt()
    private val reduceCost = setConfigurableProperty("M2", 1, ConfigurableType.Int).toInt()
    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect(caller = this) {
            addToBot {
                getAllGroup().forEach {
                    it.group.forEach { c ->
                        if (c.hasTag(AbstractCard.CardTags.STRIKE)) {
                            c.modifyCostForCombat(-reduceCost)
                            c.upDamage(damage)
                        }
                        if (c is PressurePoints) {
                            c.modifyCostForCombat(-reduceCost)
                            c.upMagicNumber(damage)
                        }
                    }
                }
            }
        })
        PatchEffect.onPostUseCardSubscribers.add(ComboEffect(caller = this) { c, t ->
            if (c is PressurePoints && !c.purgeOnUse) {
                addToTop(EmptyAction {
                    AbstractDungeon.getMonsters().monsters.forEach {
                        if (it.isAlive() && it is AbstractMonster && it != t) {
                            addToTop(
                                ApplyPowerAction(
                                    it,
                                    AbstractDungeon.player,
                                    MarkPower(it, c.magicNumber),
                                    c.magicNumber
                                )
                            )
                        }
                    }
                })
                flash()
            }
        })
    }
}