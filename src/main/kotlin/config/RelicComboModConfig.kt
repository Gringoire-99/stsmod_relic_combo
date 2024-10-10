package config

import basemod.BaseMod
import basemod.ModPanel
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig
import com.megacrit.cardcrawl.helpers.ImageMaster
import core.AbstractRelicCombo
import ui.ComboConfigurablePropertyUI
import utils.makeId
import utils.modId
import java.io.Serializable
import java.util.*

const val AddRelicToComboKey = "AddRelicToCombo"
const val RemoveRelicFromComboKey = "RemoveRelicFromCombo"

/**
 * TODO Add/Remove custom relic to combo
 */
class RelicComboModConfig {
    companion object {
        class Option<T : Serializable>(val key: String, val value: T) {
        }

        private fun <T : Serializable> Properties.setProperty(option: Option<T>) {
            setProperty(option.key, option.value.toString())
        }


        open class ComboConfig {
            companion object {
                val id = ComboConfig::class.makeId()
                val EnableCombo = Option("EnableCombo", true)
                val NumberToActive = Option("NumberToActive", 3)
                val AddRelicToComboKey = "AddRelicToCombo"
                val RemoveRelicFromComboKey = "RemoveRelicFromCombo"
                val RelicsOfThisCombo = Option("RelicsOfThisCombo", arrayListOf<String>())
            }

        }


        private var defaultConfig: Properties? = null


        var spireModConfig: SpireConfig? = null
        private var modPanel: ModPanel? = null

        fun initModMenu() {
            modPanel = ModPanel()

            modPanel?.apply {
                spireModConfig?.let {
                    addUIElement(ComboConfigurablePropertyUI(this, it))
                    BaseMod.registerModBadge(
                        ImageMaster.loadImage("$modId/ui/badge.png"), modId, "glen", "relic combo!", modPanel
                    )
                }
            }
        }

        fun <T : Serializable> getPropertyKey(combo: AbstractRelicCombo, option: Option<T>): String {
            return "${combo.id}:${option.key}"
        }

        fun loadConfig() {
            spireModConfig = SpireConfig(modId, RelicComboModConfig::class.makeId(), Properties())
            spireModConfig?.load()
        }


    }

}
