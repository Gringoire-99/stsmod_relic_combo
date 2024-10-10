package event

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.events.AbstractImageEvent
import com.megacrit.cardcrawl.relics.AbstractRelic
import utils.makeId
import utils.modId

abstract class AbstractEvent(val info: EventInfo) : AbstractImageEvent(info.title, info.intro, info.imgUrl) {

    val stages: HashMap<String, EventStage> = hashMapOf()
    var stageKey: String = ""
        set(value) {
            field = value
            updateStage()
        }
    val currentStage: EventStage?
        get() {
            return stages[stageKey]
        }

    class EventOption(
        var cardToPreview: () -> AbstractCard? = { null },
        var relicToPreview: () -> AbstractRelic? = { null },
        var text: () -> String = { "" },
        var disableText: () -> String = { "" },
        var shouldDisable: () -> Boolean = { false },
        var onSelected: (index: Int) -> Unit = {}
    )

    class EventStage(
        val key: String,
        var desc: () -> String = { "" },
        val options: ArrayList<EventOption> = arrayListOf(),
        var onEnterStage: (e: AbstractImageEvent) -> Unit = { e -> e.imageEventText.updateBodyText(desc()) }

    )

    val introStageKey = "Continue".makeId()

    fun addIntroStage(desc: () -> String = {""}, nextKey: String): EventStage {
        val eventStage = EventStage(
            key = introStageKey,
            desc = desc,
            options = arrayListOf(EventOption(text = { continueText }, onSelected = {
                stageKey = nextKey
            }))
        )
        stages[introStageKey] = eventStage
        return eventStage
    }

    val LeaveStageKey = "Leave".makeId()
    fun addLeaveStage(desc: () -> String = {""}, cb: () -> Unit = {}): EventStage {
        val eventStage = EventStage(
            key = LeaveStageKey,
            desc = desc,
            options = arrayListOf(EventOption(text = { leaveText }, onSelected = {
                cb()
                openMap()
            }))
        )
        stages[LeaveStageKey] = eventStage
        return eventStage

    }

    fun updateStage() {
        currentStage?.apply {
            onEnterStage(this@AbstractEvent)
            updateOptions()
        }
    }

    fun updateOptions() {
        currentStage?.apply {
            imageEventText.clearAllDialogs()
            options.forEach {
                it.apply {
                    val isDisable = shouldDisable()
                    imageEventText.setDialogOption(
                        if (isDisable) disableText() else text(),
                        isDisable,
                        cardToPreview(),
                        relicToPreview()
                    )
                }
            }
        }
    }

    override fun buttonEffect(index: Int) {
        currentStage?.apply {
            options.getOrNull(index)?.apply {
                onSelected(index)
            }
        }
    }


    init {
        NAME = info.title
        DESCRIPTIONS = info.desc.toTypedArray()
        OPTIONS = info.options.toTypedArray()

    }

    companion object {
        fun String.makeEventInfo(): EventInfo {
            val id = this.makeId()
            val eventString = CardCrawlGame.languagePack.getEventString(id)
            return EventInfo(
                title = eventString.NAME ?: "missing name",
                intro = eventString?.DESCRIPTIONS?.get(0) ?: "missing intro",
                imgUrl = "${modId}/images/events/${this}.jpg",
                options = ArrayList(eventString.OPTIONS.toMutableList()),
                desc = ArrayList(eventString.DESCRIPTIONS.toMutableList())
            )
        }

        val continueText = CardCrawlGame.languagePack.getEventString("CommonText".makeId()).OPTIONS[0]
        val leaveText = CardCrawlGame.languagePack.getEventString("CommonText".makeId()).OPTIONS[1]

        class EventInfo(
            var title: String,
            var intro: String,
            var imgUrl: String,
            val options: ArrayList<String>,
            val desc: ArrayList<String>
        )
    }

}