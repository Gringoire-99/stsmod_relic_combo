package combo

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.actions.utility.ScryAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import utils.addToBot
import utils.addToTop
import utils.makeId
import kotlin.math.max

class Stargazing : AbstractRelicCombo(
    Stargazing::class.makeId(),
    hashSetOf(GoldenEye.ID, Orrery.ID, FrozenEye.ID, Astrolabe.ID, BlackStar.ID, Melange.ID)
) {
    private var scryCount = 0
    override fun onBattleStart(combo: HashSet<String>) {
        scryCount += combo.size * 2
        addToTop(ScryAction(1))
    }

    override fun onBattleStartCleanup(combo: HashSet<String>) {
        scryCount = 0
    }

    override fun onScry(amount: IntArray, combo: HashSet<String>) {
        amount[0] = amount[0] + max(0, scryCount)
        addToBot(
            DamageAllEnemiesAction(
                AbstractDungeon.player,
                amount[0],
                DamageInfo.DamageType.THORNS,
                AbstractGameAction.AttackEffect.FIRE
            ),
            HealAction(AbstractDungeon.player, AbstractDungeon.player, 1)
        )
        showText()
    }
}