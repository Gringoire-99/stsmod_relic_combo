package combo

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.actions.common.GainEnergyAction
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.vfx.BorderFlashEffect
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.addToTop
import utils.drawCard
import utils.getAllGroup
import utils.makeId

class FourElement : AbstractRelicCombo(
    FourElement::class.makeId(),
    hashSetOf(
        BottledFlame.ID,
        BottledLightning.ID,
        BottledTornado.ID,
        FrozenEgg2.ID,
        MoltenEgg2.ID,
        ToxicEgg2.ID,
        PrismaticShard.ID,
        FrozenCore.ID
    ), numberToActive = 4
) {
    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect {
            flash()
            CardCrawlGame.sound.play("STANCE_ENTER_DIVINITY")
            AbstractDungeon.effectsQueue.add(BorderFlashEffect(Color.GOLD, true))
            AbstractDungeon.effectsQueue.add(
                StanceChangeParticleGenerator(
                    AbstractDungeon.player.hb.cX,
                    AbstractDungeon.player.hb.cY,
                    "Divinity"
                )
            )
            addToTop(HealAction(AbstractDungeon.player, AbstractDungeon.player, 10)) {
                getAllGroup().forEach { g ->
                    g.group.forEach {
                        if (it.canUpgrade()) {
                            it.upgrade()
                        }
                    }
                }
                val amount = AbstractDungeon.player.masterDeck.group.count { it.isInnate }
                if (amount > 0) {
                    addToTop(GainEnergyAction(amount))
                    drawCard(amount)
                }
            }
        })
    }
}