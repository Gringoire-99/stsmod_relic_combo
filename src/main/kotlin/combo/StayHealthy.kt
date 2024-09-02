package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.GainEnergyAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import utils.addToBot
import utils.addToTop
import utils.drawCard
import utils.makeId

class StayHealthy : AbstractRelicCombo(
    StayHealthy::class.makeId(),
    hashSetOf(FaceOfCleric.ID, OddMushroom.ID, MedicalKit.ID, OrangePellets.ID, Ginger.ID, Girya.ID, AncientTeaSet.ID)
) {
    override fun onBattleStart(combo: HashSet<String>) {
        addToTop {
            val h = AbstractDungeon.player.currentHealth
            val power = h / 20
            val draw = h / 30
            val e = h / 50
            if (power > 0) {
                showText()
                val s = power / 2
                val d = power - s
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
            if (draw > 0) {
                drawCard(draw)
            }
            if (e > 0) {
                addToTop(GainEnergyAction(e))
            }
        }
    }
}