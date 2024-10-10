package event

import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.EventStrings
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect
import utils.makeId
import utils.randomPick
import kotlin.math.max


class TheWorldsResponse : AbstractEvent(TheWorldsResponse::class.simpleName.toString().makeEventInfo()) {
    var damage: Int = 10
    var maxHpLoss = 5
    var heal = 20
    val damageIncrease = 4
    val maxHpLossIncrease = 3
    val healDecrease = 5

    var damageTakenTotal = 0
    var damageHealTotal = 0
    var maxhpLossTotal = 0
    var cardsUpgradedTotal: ArrayList<String> = arrayListOf()
    var relicObtainedTotal: ArrayList<String> = arrayListOf()
    var relicLostTotal: ArrayList<String> = arrayListOf()

    var relicToDestroy: AbstractRelic? = null
    var relicToTransform: AbstractRelic? = null


    private fun updateRelic() {
        relicToDestroy = AbstractDungeon.player.relics.randomPick().firstOrNull()
        AbstractDungeon.player.relics.filter { it.tier != RelicTier.STARTER }.apply {
            relicToTransform = randomPick().firstOrNull()
        }
    }

    init {
        stageKey = addIntroStage({ info.intro }, nextKey = ID).key
        addLeaveStage({ info.desc.last() }) {
            // not working
            logMetric(
                info.title,
                info.title,
                null, null, null, cardsUpgradedTotal, relicObtainedTotal,
                null, relicLostTotal,
                damageTakenTotal, damageHealTotal, maxhpLossTotal, 0,
                0, 0
            )
        }
        stages[ID] =
            EventStage(
                ID, desc = { info.desc[1] }, options = arrayListOf(
                    EventOption(text = { info.options[0].replace(" !M! ", "$damage") }) {
                        AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier())
                            .apply {
                                relicObtainedTotal.add(relicId)
                                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
                                    (Settings.WIDTH / 2).toFloat(),
                                    (Settings.HEIGHT / 2).toFloat(), this.makeCopy()
                                )
                            }
                        AbstractDungeon.player.damage(DamageInfo(null, damage, DamageInfo.DamageType.HP_LOSS))
                        damageTakenTotal += damage
                        imageEventText.updateBodyText(info.desc[2])
                        increaseAllEffect()
                    },
                    EventOption(
                        text = { info.options[1].replace(" !M! ", relicToDestroy?.name ?: "???") },
                        disableText = { info.options.last() },
                        shouldDisable = { relicToDestroy == null },
                        relicToPreview = { relicToDestroy }
                    ) {

                        relicToDestroy?.apply {
                            if (AbstractDungeon.player.loseRelic(this.relicId)) {
                                repeat(3) {
                                    var c: AbstractCard? = null
                                    AbstractDungeon.player.masterDeck.group.filter { it.canUpgrade() }.apply {
                                        c = getOrNull(
                                            AbstractDungeon.miscRng.random(0, max(0, size - 1))
                                        )
                                    }
                                    c?.apply {
                                        val x = MathUtils.random(0.1f, 0.9f) * Settings.WIDTH.toFloat()
                                        val y = MathUtils.random(0.2f, 0.8f) * Settings.HEIGHT.toFloat()
                                        upgrade()
                                        AbstractDungeon.effectList.add(
                                            ShowCardBrieflyEffect(
                                                makeStatEquivalentCopy(),
                                                x,
                                                y
                                            )
                                        )
                                        AbstractDungeon.topLevelEffects.add(UpgradeShineEffect(x, y))
                                        cardsUpgradedTotal.add(cardID)
                                        relicLostTotal.add(relicId)
                                    }
                                }

                            }
                        }
                        imageEventText.updateBodyText(info.desc[3])
                        increaseAllEffect()
                    },
                    EventOption(
                        text = {
                            info.options[2].replace(" !M! ", relicToTransform?.name ?: "???")
                                .replace(" !M2! ", "$maxHpLoss")
                        },
                        disableText = { info.options.last() },
                        shouldDisable = { AbstractDungeon.player.maxHealth < maxHpLoss || relicToTransform == null },
                        relicToPreview = { relicToTransform }
                    ) {
                        relicToTransform?.apply {
                            if (AbstractDungeon.player.loseRelic(relicId) && AbstractDungeon.player.maxHealth > maxHpLoss) {
                                relicLostTotal.add(relicId)
                                maxhpLossTotal += maxHpLoss
                                repeat(2) {
                                    AbstractDungeon.returnRandomRelic(tier).apply {
                                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
                                            (Settings.WIDTH / 2).toFloat(),
                                            (Settings.HEIGHT / 2).toFloat(), this.makeCopy()
                                        )
                                    }
                                }
                                AbstractDungeon.player.decreaseMaxHealth(maxHpLoss)
                            }
                        }
                        imageEventText.updateBodyText(info.desc[4])
                        increaseAllEffect()
                    },
                    EventOption(text = {
                        info.options[3].replace(" !M! ", "$heal")
                    }) {
                        if (heal >= 0) {
                            damageHealTotal += heal
                            AbstractDungeon.player.heal(heal)
                        } else {
                            damageTakenTotal -= heal
                            AbstractDungeon.player.damage(DamageInfo(null, -heal, DamageInfo.DamageType.HP_LOSS))
                        }
                        imageEventText.updateBodyText(info.desc[5])
                        stageKey = LeaveStageKey
                    }
                )
            ) {
                imageEventText.updateBodyText(info.desc[1])
                updateRelic()
            }
    }

    companion object {
        @JvmField
        val ID = TheWorldsResponse::class.makeId()

        @JvmField
        val eventStrings: EventStrings? = CardCrawlGame.languagePack.getEventString(ID)

        @JvmField
        val NAME: String? = eventStrings?.NAME

        @JvmField
        val DESCRIPTIONS: Array<out String>? = eventStrings?.DESCRIPTIONS

        @JvmField
        val OPTIONS: Array<out String>? = eventStrings?.OPTIONS
    }

    private fun increaseAllEffect() {
        damage += damageIncrease
        maxHpLoss += maxHpLossIncrease
        heal -= healDecrease
        updateRelic()
        updateOptions()
    }
}