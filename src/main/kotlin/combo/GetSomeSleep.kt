package combo

import action.EmptyEffect
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.AncientTeaSet
import com.megacrit.cardcrawl.relics.DreamCatcher
import com.megacrit.cardcrawl.relics.RegalPillow
import com.megacrit.cardcrawl.relics.UnceasingTop
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.makeId
import kotlin.math.max

class GetSomeSleep : AbstractRelicCombo(
    GetSomeSleep::class.makeId(),
    hashSetOf(AncientTeaSet.ID, DreamCatcher.ID, RegalPillow.ID, UnceasingTop.ID)
) {
    override fun onActive() {
        PatchEffect.onPreRestSubscribers.add(ComboEffect { healAmount ->
            flash()
            val a: Int = max(healAmount, AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth)
            AbstractDungeon.effectsQueue.add(EmptyEffect {
                AbstractDungeon.player.increaseMaxHp(10, true)
            })
            repeat(2 + getExtraCollectCount()) {
                val group = AbstractDungeon.player.masterDeck.group.filter { it.canUpgrade() }.groupBy { it.rarity }
                val s =
                    hashSetOf(
                        CardRarity.RARE,
                        CardRarity.UNCOMMON,
                        CardRarity.COMMON,
                        CardRarity.BASIC,
                        CardRarity.SPECIAL,
                        CardRarity.CURSE
                    )
                var c: AbstractCard? = null
                s.any {
                    val randomOrNull = group[it]?.randomOrNull()
                    if (randomOrNull != null) {
                        c = randomOrNull
                    }
                    randomOrNull != null
                }
                c?.apply {
                    upgrade()
                    AbstractDungeon.effectsQueue.add(ShowCardBrieflyEffect(makeStatEquivalentCopy()))
                    AbstractDungeon.topLevelEffectsQueue.add(
                        UpgradeShineEffect(
                            Settings.WIDTH.toFloat() / 2.0f,
                            Settings.HEIGHT.toFloat() / 2.0f
                        )
                    )
                }
            }
            a
        })
    }
}