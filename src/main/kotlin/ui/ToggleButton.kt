package ui

import basemod.IUIElement
import basemod.ModLabeledToggleButton
import basemod.ModPanel
import basemod.ModToggleButton
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper

class ToggleButton(
    parent: ModPanel,
    initialVal: Boolean,
    label: String = "",
    private val layer: Int = 0,
    private val order: Int = 0,
    xPos: Float,
    yPos: Float,
    color: Color = Settings.CREAM_COLOR,
    onChange: (b: ModToggleButton) -> Unit
) : IUIElement {
    var value: Boolean = initialVal
        set(value) {
            field = value
            toggleBtn.toggle.enabled = field
        }
    val toggleBtn = ModLabeledToggleButton(
        label, xPos, yPos, color, FontHelper.charDescFont, value, parent, {}, { b ->
            if (value != b.enabled) {
                onChange(b)
            }
            value = b.enabled
        }
    )

    override fun render(sb: SpriteBatch?) {
        toggleBtn.render(sb)
    }

    override fun update() {
        toggleBtn.update()
    }

    override fun renderLayer(): Int {
        return layer
    }

    override fun updateOrder(): Int {
        return order
    }
}