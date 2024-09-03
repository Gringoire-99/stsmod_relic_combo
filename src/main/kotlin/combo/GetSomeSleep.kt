package combo

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
import utils.makeId
import utils.toLog
import kotlin.math.max

class GetSomeSleep : AbstractRelicCombo(
    GetSomeSleep::class.makeId(),
    hashSetOf(AncientTeaSet.ID, DreamCatcher.ID, RegalPillow.ID, UnceasingTop.ID)
) {
    override fun onRest(healAmount: IntArray, combo: HashSet<String>) {
        showText()
        healAmount[0] = max(healAmount[0], AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth)
        (1 + max(0, combo.size - numberToActive)).toLog("升级次数")
        repeat(1 + max(0, combo.size - numberToActive)) {
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
            c.toLog("被升级的卡")
            c?.apply {
                upgrade()
                AbstractDungeon.effectsQueue.add(ShowCardBrieflyEffect(makeStatEquivalentCopy()))
                AbstractDungeon.topLevelEffects.add(
                    UpgradeShineEffect(
                        Settings.WIDTH.toFloat() / 2.0f,
                        Settings.HEIGHT.toFloat() / 2.0f
                    )
                )
            }
        }
    }
}