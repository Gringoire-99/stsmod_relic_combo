package combo

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.GainEnergyAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.LoseStrengthPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.relics.*
import core.AbstractRelicCombo
import core.ComboEffect
import core.ConfigurableType
import core.PatchEffect
import utils.addToBot
import utils.drawCard
import utils.makeId

class Dispel : AbstractRelicCombo(
    Dispel::class.makeId(),
    hashSetOf(
        Necronomicon.ID,
        CursedKey.ID,
        CallingBell.ID,
        DuVuDoll.ID,
        BlueCandle.ID,
        DarkstonePeriapt.ID,
        Omamori.ID
    )
) {
    private val m = setConfigurableProperty("M", 1, ConfigurableType.Int).toInt()
    private val gain = setConfigurableProperty("M2", 1, ConfigurableType.Int).toInt()
    private val s = setConfigurableProperty("M3", 2, ConfigurableType.Int).toInt()

    private var isUsed: Boolean = false
    override fun onActive() {
        PatchEffect.onPostStartOfTurnSubscribers.add(ComboEffect {
            isUsed = false
        })
        PatchEffect.onPostDrawnCardSubscribers.add(ComboEffect {
            if (it.type == AbstractCard.CardType.CURSE) {
                drawCard(m)
                addToBot(
                    ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        StrengthPower(AbstractDungeon.player, m)
                    ),
                    ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        LoseStrengthPower(AbstractDungeon.player, m)
                    )
                )
                flash()
            }
        })
        PatchEffect.onPostExhaustCardSubscribers.add(ComboEffect {
            if (!isUsed && it.type == AbstractCard.CardType.CURSE) {
                addToBot(
                    GainEnergyAction(gain),
                    ApplyPowerAction(
                        AbstractDungeon.player,
                        AbstractDungeon.player,
                        StrengthPower(AbstractDungeon.player, s)
                    ),
                )
                isUsed = true
                flash()
            }
        })
    }
}