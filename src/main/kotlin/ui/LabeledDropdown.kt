package ui

import basemod.IUIElement
import basemod.ModLabel
import basemod.ModPanel
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.screens.options.DropdownMenu
import com.megacrit.cardcrawl.screens.options.DropdownMenuListener

class LabeledDropdown(
    val parent: ModPanel,
    val xPos: Float,
    val yPos: Float,
    val labelText: String = "",
    val tooltip: String = "",
    val options: ArrayList<String> = arrayListOf(),
    font: BitmapFont = FontHelper.charDescFont,
    color: Color = Color.LIGHT_GRAY,
    val onUpdate: (DropdownMenu) -> Unit = { _ -> },
    val onSelected: (Int, String?) -> Unit = { _, _ -> }
) : IUIElement, DropdownMenuListener {

    val dropdownMenu: DropdownMenu = DropdownMenu(this, options, font, color)
    var label: ModLabel? = null

    init {
        if (labelText != "") {
            label = ModLabel(labelText, xPos, yPos + 15, color, font, parent) {}
        }

    }

    override fun render(p0: SpriteBatch?) {
        label?.render(p0)
        dropdownMenu.render(p0, xPos * Settings.xScale, yPos * Settings.yScale)
    }

    override fun update() {
        dropdownMenu.update()
        onUpdate(dropdownMenu)
        label?.update()
    }

    override fun renderLayer(): Int {
        return 1
    }

    override fun updateOrder(): Int {
        return 1
    }

    override fun changedSelectionTo(p0: DropdownMenu?, index: Int, text: String?) {
        onSelected(index, text)
    }
}