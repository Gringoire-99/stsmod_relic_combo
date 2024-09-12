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
//                    //Enable Combo
//                    val enableComboToggleButton =
//                        ToggleButton(label = comboConfigText.getOrDefault(ComboConfig.EnableCombo.key, "??"),
//                            xPos = firstX,
//                            yPos = firstY,
//                            initialVal = it.getBool(getPropertyKey(options.get(index), ComboConfig.EnableCombo)),
//                            parent = this,
//                            onChange = { b ->
//                                it.apply {
//                                    setBool(getPropertyKey(options.get(index), ComboConfig.EnableCombo), b.enabled)
//                                    save()
//                                    AbstractRelicCombo.updateEnabledRelicComboSets()
//                                }
//                            })
//                    //Desc
//                    addUIElement(HeadlessTip(xPos = firstX + 400, yPos = firstY) {
//                        options.get(index).desc
//                    })
//
//                    val numberSelector = NumberSelector(
//                        it.getInt(
//                            getPropertyKey(
//                                options.get(index),
//                                ComboConfig.NumberToActive
//                            )
//                        ),
//                        label = comboConfigText.get(ComboConfig.NumberToActive.key) ?: "label not find!",
//                        range = 1..Int.MAX_VALUE,
//                        xPos = firstX,
//                        yPos = firstY - 100,
//                        onChange = { _, new ->
//                            it.apply {
//                                setInt(getPropertyKey(options.get(index), ComboConfig.NumberToActive), new)
//                                save()
//                                AbstractRelicCombo.updateEnabledRelicComboSets()
//                            }
//                        }
//                    )
//                    //Dropdown to select current combo
//                    addUIElement(LabeledDropdown(
//                        parent = this,
//                        options = ArrayList(options.map { it.title }),
//                        xPos = firstX + 800,
//                        yPos = firstY + 50
//                    ) { i, _ ->
//                        index = i
//                        enableComboToggleButton.value =
//                            it.getBool(
//                                getPropertyKey(
//                                    options.get(index),
//                                    ComboConfig.EnableCombo
//                                )
//                            ) == true
//                        numberSelector.value = it.getInt(
//                            getPropertyKey(
//                                options.get(index),
//                                ComboConfig.NumberToActive
//                            )
//                        )
//                    })
//                    addUIElement(
//                        enableComboToggleButton
//                    )
//                    addUIElement(
//                        numberSelector
//                    )
                    BaseMod.registerModBadge(
                        ImageMaster.loadImage("$modId/ui/badge.png"), modId, "glen", "relic combo!", modPanel
                    )
                }

            }

        }

        fun <T : Serializable> getPropertyKey(combo: AbstractRelicCombo, option: Option<T>): String {
            return "${combo.id}:${option.key}"
        }

//        private fun createDefaultConfig() {
//            val config = Properties()
////            AbstractRelicCombo.registeredComboSet.forEach { s ->
////                config.setProperty(getPropertyKey(s, ComboConfig.EnableCombo), true.toString())
////                config.setProperty(getPropertyKey(s, ComboConfig.NumberToActive), s.numberToActive.toString())
////                config.setProperty(
////                    getPropertyKey(s, ComboConfig.RelicsOfThisCombo),
////                    Json().toJson(s.combo.toArray())
////                )
////            }
//            defaultConfig = config
//        }

        fun loadConfig() {
            spireModConfig = SpireConfig(modId, RelicComboModConfig::class.makeId(), Properties())
            spireModConfig?.load()
        }


    }

}
