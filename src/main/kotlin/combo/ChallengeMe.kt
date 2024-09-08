package combo

import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.PatchEffect
import utils.drawCard
import utils.isInCombat
import utils.makeId
import kotlin.math.ceil
import kotlin.math.max

class ChallengeMe :
    AbstractRelicCombo(
        ChallengeMe::class.makeId(),
        hashSetOf(ChampionsBelt.ID, SlaversCollar.ID, Sling.ID, Pantograph.ID, GremlinHorn.ID, PreservedInsect.ID)
    ) {
    private var active: Boolean = false

    override fun onActive() {

        PatchEffect.onPostBattleStartCleanupSubscribers.add(ComboEffect {
            active =
                AbstractDungeon.getMonsters().monsters.any { it.type == AbstractMonster.EnemyType.BOSS || it.type == AbstractMonster.EnemyType.ELITE }
        })
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect {
            if (active) {
                drawCard(1)
                flash()
            }
        })
        PatchEffect.onPreMonsterTakingDamageSubscribers.add(ComboEffect(priority = 0) { damage, _ ->
            var d = damage
            if (active) {
                d += max(0, ceil(damage * 0.05 * getCurrentComboSize()).toInt())
                flash()
            }
            d
        })
        PatchEffect.onPrePlayerTakingDamageSubscribers.add(ComboEffect(priority = 0) { damage, _ ->
            var d = damage
            if (active && isInCombat()) {
                d = max(0, damage - ceil(damage * 0.05 * getCurrentComboSize()).toInt())
                flash()
            }
            d
        })
    }
}