package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.defect.ImpulseAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.FocusPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.addToBot
import utils.addToQueue
import utils.makeId

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
    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect {
            flash()
            repeat(1 + getExtraCollectCount()) {
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
        })
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect {
            flash()
            val c = 1
            repeat(c) {
                addToBot(
                    ImpulseAction(),
                )
            }
        })
        PatchEffect.onPostUseCardSubscribers.add(ComboEffect { c, t ->
            if (c.type == AbstractCard.CardType.ATTACK && !c.purgeOnUse) {
                flash()
                addToQueue(c, t, random = false, purge = true)
            }
        })
    }

}