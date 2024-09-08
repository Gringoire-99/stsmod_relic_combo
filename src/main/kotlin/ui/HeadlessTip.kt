package ui

import basemod.IUIElement
import basemod.patches.com.megacrit.cardcrawl.helpers.TipHelper.HeaderlessTip
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.Settings

class HeadlessTip(
    private val layer: Int = 0,
    private val order: Int = 0,
    val xPos: Float,
    val yPos: Float,
    val onUpdate: () -> Unit = {},
    val onRender: (x: Float, y: Float, text: String) -> Unit = { _, _, _ -> },
    val text: () -> String
) : IUIElement {
    override fun render(p0: SpriteBatch?) {
        val text1 = text()
        onRender(xPos, yPos, text1)
        HeaderlessTip.renderHeaderlessTip(xPos * Settings.xScale, yPos * Settings.yScale, text1)
    }

    override fun update() {
        onUpdate()
    }

    override fun renderLayer(): Int {
        return layer
    }

    override fun updateOrder(): Int {
        return order
    }
}