package combo

import com.megacrit.cardcrawl.relics.*
import utils.makeId

class ChallengeMe :
    AbstractRelicCombo(
        ChallengeMe::class.makeId(),
        hashSetOf(ChampionsBelt.ID, SlaversCollar.ID, Sling.ID, Pantograph.ID, GremlinHorn.ID, PreservedInsect.ID)
    ) {
}