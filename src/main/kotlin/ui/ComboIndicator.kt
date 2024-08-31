package ui

import basemod.TopPanelItem
import com.badlogic.gdx.graphics.Texture
import utils.makeId
import utils.modId

class ComboIndicator : TopPanelItem(
    Texture("${modId}/ui/${ComboIndicator::class.simpleName.toString()}.png"),
    ComboIndicator::class.makeId()
) {

    override fun onClick() {

    }

    override fun onHover() {
        super.onHover()
    }
}