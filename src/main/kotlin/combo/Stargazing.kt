package combo

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction
import com.megacrit.cardcrawl.actions.common.HealAction
import com.megacrit.cardcrawl.actions.utility.ScryAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.addToBot
import utils.addToTop
import utils.makeId
import kotlin.math.max

class Stargazing : AbstractRelicCombo(
    Stargazing::class.makeId(),
    hashSetOf(GoldenEye.ID, Orrery.ID, FrozenEye.ID, Astrolabe.ID, BlackStar.ID, Melange.ID)
) {
    private val scryAdd = setConfigurableProperty("M", 2, ConfigurableType.Int).toInt()
    private val m = setConfigurableProperty("M2", 1, ConfigurableType.Int).toInt()
    private var scryCount = 0
    override fun onActive() {
        PatchEffect.onPostBattleStartSubscribers.add(ComboEffect(caller = this) {
            scryCount += getCurrentComboSize() * scryAdd
            addToTop(ScryAction(m))
        })
        PatchEffect.onPostBattleStartCleanupSubscribers.add(ComboEffect(caller = this) {
            scryCount = 0
        })
        PatchEffect.onPreScrySubscribers.add(ComboEffect(caller = this) { amount ->
            val a: Int = amount + max(0, scryCount)
            addToBot(
                DamageAllEnemiesAction(
                    AbstractDungeon.player,
                    a,
                    DamageInfo.DamageType.THORNS,
                    AbstractGameAction.AttackEffect.FIRE
                ),
                HealAction(AbstractDungeon.player, AbstractDungeon.player, m)
            )
            flash()
            a
        })
    }

}