package event

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity
import com.megacrit.cardcrawl.cards.curses.Clumsy
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.CardLibrary
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.localization.EventStrings
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier
import com.megacrit.cardcrawl.rewards.RewardItem
import com.megacrit.cardcrawl.vfx.BorderFlashEffect
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect
import utils.makeId
import utils.randomPick
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class MirrorOfLoss : AbstractEvent(MirrorOfLoss::class.simpleName.toString().makeEventInfo()) {

    var relicToCopy: AbstractRelic? = null
    var maxHpToLosePercent = 5
    var maxHpToLose = 0
    var cardsToLose: ArrayList<AbstractCard> = arrayListOf()
    val goldGain = 75
    var leaveDesc = info.desc.last()

    init {
        stageKey = addIntroStage(desc = { info.intro }, nextKey = ID).key
        addLeaveStage(desc = { leaveDesc })
        stages[ID] = EventStage(
            key = ID, desc = { info.desc[1] }, options = arrayListOf(
                EventOption(
                    relicToPreview = { relicToCopy },
                    text = {
                        info.options[0].replace(" !M! ", relicToCopy?.name ?: "???")
                            .replace(" !M2! ", maxHpToLose.toString())
                    },
                    shouldDisable = { relicToCopy == null },
                    disableText = { info.options[3] }
                ) {
                    relicToCopy?.apply {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
                            (Settings.WIDTH / 4).toFloat(),
                            (Settings.HEIGHT / 2).toFloat(), RelicLibrary.getRelic(relicId).makeCopy()
                        )
                        AbstractDungeon.player.decreaseMaxHealth(maxHpToLose)
                    }
                    leaveDesc = info.desc[2]
                    stageKey = LeaveStageKey
                },
                EventOption(cardToPreview = { Clumsy() }, text = {
                    info.options[1].replace(" !M! ", goldGain.toString())
                }) {
                    CardCrawlGame.sound.play("BLUNT_FAST")
                    AbstractDungeon.topLevelEffects.add(BorderFlashEffect(Color(1.0f, 0.1f, 0.05f, 0.0f)))
                    AbstractDungeon.effectList.add(
                        ShowCardAndObtainEffect(
                            CardLibrary.getCopy(Clumsy.ID),
                            (Settings.WIDTH / 2).toFloat(),
                            (Settings.HEIGHT / 2).toFloat()
                        )
                    )
                    repeat(2) {
                        var c: AbstractCard?
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
                        }
                    }
                    AbstractDungeon.getCurrRoom().addCardReward(RewardItem())
                    AbstractDungeon.getCurrRoom().addGoldToRewards(goldGain)
                    AbstractDungeon.combatRewardScreen.open()
                    leaveDesc = info.desc[3]
                    stageKey = LeaveStageKey
                },
                EventOption(text = {
                    info.options[2].replace(" !M! ", cardsToLose.getOrNull(0)?.name ?: "???")
                        .replace(" !M2! ", cardsToLose.getOrNull(1)?.name ?: "???")
                        .replace(" !M3! ", cardsToLose.getOrNull(2)?.name ?: "???")
                }, shouldDisable = { cardsToLose.size < 3 }, disableText = { info.options.last() }) {
                    cardsToLose.forEach {
                        val x = MathUtils.random(0.1f, 0.9f) * Settings.WIDTH.toFloat()
                        val y = MathUtils.random(0.2f, 0.8f) * Settings.HEIGHT.toFloat()
                        AbstractDungeon.topLevelEffects.add(
                            PurgeCardEffect(
                                it,
                                x,
                                y
                            )
                        )
                        AbstractDungeon.player.masterDeck.removeCard(it)
                    }
                    AbstractDungeon.getCurrRoom().addCardReward(RewardItem(CardColor.COLORLESS))
                    AbstractDungeon.combatRewardScreen.open()
                    leaveDesc = info.desc[4]
                    stageKey = LeaveStageKey
                },
                EventOption(text = { leaveText }) {
                    leaveDesc = info.desc[5]
                    stageKey = LeaveStageKey
                }
//           这里可以实现一个死灵之书的彩蛋？ 但暂时没想好什么效果
            )
        ) {
            imageEventText.updateBodyText(info.desc[1])
            AbstractDungeon.player.relics.apply {
                relicToCopy = getOrNull(AbstractDungeon.miscRng.random(0, max(size - 1, 0)))
                relicToCopy?.apply {
                    if (tier == RelicTier.UNCOMMON) {
                        maxHpToLosePercent *= 2
                    } else if (tier == RelicTier.RARE) {
                        maxHpToLosePercent *= 3
                    } else if (tier == RelicTier.BOSS) {
                        maxHpToLosePercent *= 4
                    }
                    maxHpToLose = round(AbstractDungeon.player.maxHealth * 0.01f * maxHpToLosePercent).toInt()
                }
            }
            AbstractDungeon.player.masterDeck.purgeableCards.group.apply {
                filter { it.rarity == CardRarity.UNCOMMON && it !in cardsToLose }.randomPick()
                    .let { cardsToLose.addAll(it) }
                filter { it.rarity == CardRarity.COMMON && it !in cardsToLose }.randomPick()
                    .let { cardsToLose.addAll(it) }
                var fill = min(3, max(0, 3 - cardsToLose.size))
                filter { it.rarity == CardRarity.BASIC && it !in cardsToLose }.randomPick(fill)
                    .let { cardsToLose.addAll(it) }
                fill = min(3, max(0, 3 - cardsToLose.size))
                filter { it.rarity == CardRarity.COMMON && it !in cardsToLose }.randomPick(fill)
                    .let { cardsToLose.addAll(it) }
                fill = min(3, max(0, 3 - cardsToLose.size))
                filter { it.rarity == CardRarity.UNCOMMON && it !in cardsToLose }.randomPick(fill)
                    .let { cardsToLose.addAll(it) }
            }
        }
    }

    companion object {
        @JvmField
        val ID = MirrorOfLoss::class.makeId()

        @JvmField
        val eventStrings: EventStrings? = CardCrawlGame.languagePack.getEventString(ID)

        @JvmField
        val NAME: String? = eventStrings?.NAME

        @JvmField
        val DESCRIPTIONS: Array<out String>? = eventStrings?.DESCRIPTIONS

        @JvmField
        val OPTIONS: Array<out String>? = eventStrings?.OPTIONS
    }

}