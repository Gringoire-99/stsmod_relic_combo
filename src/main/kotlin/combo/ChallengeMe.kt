package combo

import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.relics.*
import com.megacrit.cardcrawl.rooms.AbstractRoom
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

    override fun onBattleStartCleanup(combo: HashSet<String>) {
        if (AbstractDungeon.getMonsters().monsters.any { it.type == AbstractMonster.EnemyType.BOSS || it.type == AbstractMonster.EnemyType.ELITE }) {
            active = true
        }
    }

    override fun onEndBattle(room: AbstractRoom, combo: HashSet<String>) {
        active = false
    }

    override fun onStartOfTurn(combo: HashSet<String>) {
        if (active) {
            drawCard(1)
            showText()
        }
    }

    override fun onMonsterTakingDamageStart(info: DamageInfo, damageAmount: IntArray, combo: HashSet<String>) {
        if (active && info.owner is AbstractPlayer) {
            damageAmount[0] += max(0, ceil(damageAmount[0] * 0.25).toInt())
        }
    }

    override fun onPlayerTakingDamageFinal(damage: IntArray, combo: HashSet<String>) {
        if (active && isInCombat()) {
            damage[0] = max(0, damage[0] - ceil(damage[0] * 0.25).toInt())
        }
    }
}