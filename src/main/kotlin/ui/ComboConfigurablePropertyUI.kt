package ui

import basemod.IUIElement
import basemod.ModPanel
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.relics.AbstractRelic
import core.AbstractRelicCombo
import core.ConfigurableType

class ComboConfigurablePropertyUI(val parent: ModPanel, val config: SpireConfig) : IUIElement {
    private val firstX = 400f
    private val firstY = 750f
    private var options: ArrayList<AbstractRelicCombo> = ArrayList(AbstractRelicCombo.registeredComboSet)
    private var index = 0
        set(value) {
            field = value
            generateConfigurableUI()
            updateCurrent()
        }
    private val currentCombo: AbstractRelicCombo
        get() {
            return options[index]
        }
    private val desc: HeadlessTip = HeadlessTip(xPos = firstX + 400, yPos = firstY ) {
        currentCombo.desc
    }
    private val currentComboDropdown: LabeledDropdown = LabeledDropdown(
        parent = parent, options = ArrayList(options.map { it.title }), xPos = firstX + 800, yPos = firstY
    ) { i, _ ->
        index = i
    }
    private val toggles: ArrayList<ToggleButton> = arrayListOf()
    private val numberSelectors: ArrayList<NumberSelector> = arrayListOf()
    val relics: ArrayList<AbstractRelic> = arrayListOf()
    private fun updateCurrent() {
        options = ArrayList(AbstractRelicCombo.registeredComboSet)
        currentCombo.updateTip()
        relics.clear()
        currentCombo.combo.forEach {
            relics.add(RelicLibrary.getRelic(it).makeCopy())
        }
        relics.forEachIndexed { index, abstractRelic ->
            val col = index % 5
            val row = index / 5
            abstractRelic.currentX = firstX + 400 + col * 70f
            abstractRelic.currentY = firstY - 300 - row * 70f
        }
    }

    private fun getLocate(): Locate {
        val size = toggles.size + numberSelectors.size
        val col = size / 7
        val row = size % 7
        return Locate(firstX + col * 200f, firstY - row * 100F)
    }

    class Locate(val x: Float, val y: Float)

    init {
        generateConfigurableUI()
        updateCurrent()
    }

    private fun generateConfigurableUI() {
        toggles.clear()
        numberSelectors.clear()
        val combo = options[index]
        combo.configurablePropertySet.values.sortedBy { it.type }.forEach {
            val locate = getLocate()
            when (it.type) {
                ConfigurableType.Bool -> toggles.add(
                    ToggleButton(label = it.name,
                        xPos = locate.x,
                        yPos = locate.y,
                        initialVal = config.getString(it.key).toBoolean(),
                        parent = parent,
                        onChange = { b ->
                            config.apply {
                                setString(it.key, b.enabled.toString())
                                save()
                                AbstractRelicCombo.updateEnabledRelicComboSets()
                                updateCurrent()
                            }
                        })
                )

                ConfigurableType.Int -> numberSelectors.add(
                    NumberSelector(initialVal = config.getString(it.key).toInt(),
                        label = it.name,
                        range = 0..Int.MAX_VALUE,
                        xPos = locate.x,
                        yPos = locate.y,
                        onChange = { _, new ->
                            config.apply {
                                setString(it.key, new.toString())
                                save()
                                AbstractRelicCombo.updateEnabledRelicComboSets()
                                updateCurrent()
                            }
                        })
                )
            }
        }
    }


    override fun render(sb: SpriteBatch?) {
        desc.render(sb)
        currentComboDropdown.render(sb)
        toggles.forEach { it.render(sb) }
        numberSelectors.forEach {
            it.render(sb)
        }
        relics.forEach {
            it.renderWithoutAmount(sb, Color.LIGHT_GRAY)
        }

    }

    override fun update() {
        desc.update()
        currentComboDropdown.update()
        toggles.forEach { it.update() }
        numberSelectors.forEach { it.update() }
        relics.forEach { it.update() }
    }

    override fun renderLayer(): Int {
        return 0
    }

    override fun updateOrder(): Int {
        return 0
    }
}

