package ui

import basemod.TopPanelItem
import com.badlogic.gdx.graphics.Texture
import utils.makeId
import utils.modId

/**
 * TODO widget that can check current combo
 */
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