import basemod.BaseMod
import basemod.interfaces.EditStringsSubscriber
import basemod.interfaces.PostInitializeSubscriber
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.localization.RelicStrings
import com.megacrit.cardcrawl.localization.UIStrings
import config.RelicComboModConfig
import core.AbstractRelicCombo
import core.PatchEffect
import utils.modId
import utils.toLog

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
    }
}



