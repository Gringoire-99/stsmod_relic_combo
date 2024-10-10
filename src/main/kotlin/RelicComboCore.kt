import basemod.BaseMod
import basemod.eventUtil.AddEventParams
import basemod.interfaces.EditStringsSubscriber
import basemod.interfaces.PostInitializeSubscriber
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.localization.EventStrings
import com.megacrit.cardcrawl.localization.RelicStrings
import com.megacrit.cardcrawl.localization.UIStrings
import config.RelicComboModConfig
import core.AbstractRelicCombo
import core.PatchEffect
import event.MirrorOfLoss
import event.TheWorldsResponse
import utils.modId

@SpireInitializer
class RelicComboCore : EditStringsSubscriber,
    PostInitializeSubscriber {
    init {
        BaseMod.subscribe(this)
        BaseMod.subscribe(PatchEffect.patchInstance)
    }

    companion object {
        @JvmStatic
        fun initialize() {
            RelicComboCore()
        }
    }


    override fun receiveEditStrings() {
        val lang: String = getLangSupport()
        val prefix = "${modId}/localization/$lang/"
        BaseMod.loadCustomStringsFile(RelicStrings::class.java, "${prefix}relics.json")
        BaseMod.loadCustomStringsFile(UIStrings::class.java, "${prefix}ui.json")
        BaseMod.loadCustomStringsFile(EventStrings::class.java, "${prefix}events.json")
    }

    private fun getLangSupport(): String {
        return when (Settings.language) {
            Settings.GameLanguage.ZHS -> "ZHS"
            Settings.GameLanguage.ENG -> "ENG"
            else -> {
                "ENG"
            }
        }
    }

    override fun receivePostInitialize() {
        RelicComboModConfig.loadConfig()
        AbstractRelicCombo.registerComboSet(
            AbstractRelicCombo.getAllDefaultComboSet()
        )
        RelicComboModConfig.initModMenu()
        AbstractRelicCombo.updateEnabledRelicComboSets()
        BaseMod.addEvent(
            AddEventParams.Builder(TheWorldsResponse.ID, TheWorldsResponse::class.java)
                .spawnCondition { AbstractDungeon.actNum == 3 }.create()
        )
        BaseMod.addEvent(
            AddEventParams.Builder(MirrorOfLoss.ID, MirrorOfLoss::class.java)
                .spawnCondition { AbstractDungeon.actNum == 2 }.create()
        )
    }
}



