import basemod.BaseMod
import basemod.interfaces.EditStringsSubscriber
import basemod.interfaces.PostInitializeSubscriber
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.localization.RelicStrings
import combo.*
import utils.modId


@SpireInitializer
class RelicComboCore : EditStringsSubscriber,
    PostInitializeSubscriber {
    init {
        BaseMod.subscribe(this)
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
        BaseMod.loadCustomStringsFile(RelicStrings::class.java, "${prefix}combo.json")
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
        AbstractRelicCombo.registerComboSet(
            Alchemy(),
            AncientCreation(),
            Awakened(),
            BringInTheBucks(),
            ChallengeMe(),
            CounterAttack(),
            DeepMeditation(),
            Dispel(),
            Eternal(),
            FourElement(),
            GetSomeSleep(),
            Innocence(),
            KnowledgeIsPower(),
            KungFu(),
            LessIsMore(),
            Library(),
            Mechanism(),
            MightFortune(),
            Ninja(),
            OhNo(),
            PainIsPower(),
            Plain(),
            Repair(),
            SetSail(),
            Stargazing(),
            StayHealthy(),
            TheThreeOfUs(),
            ToxinPurification(),
            VIP(),
//            StayUp(),
            Yummy()
        )

    }

}