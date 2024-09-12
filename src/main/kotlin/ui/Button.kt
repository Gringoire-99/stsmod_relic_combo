package ui

import basemod.IUIElement
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.input.InputHelper
import java.util.*


abstract class AbstractButton(
    val width: Float, val height: Float, val label: String = "",
    xPos: Float, yPos: Float, var onPress: () -> Unit = {},
    var onRelease: () -> Unit = {},
    var onHold: () -> Unit = {},
    var onRightClick: () -> Unit = {},
    var onRightRelease: () -> Unit = {}
) : IUIElement {
    var hb: Hitbox = Hitbox(width, height)
init {
    x = xPos
    y = yPos
}

    var isHidden: Boolean = false

    var texture: Texture? = null
    var textureRegion: TextureRegion? = null


    var pressStarted: Boolean = false


    override fun render(sb: SpriteBatch) {
        hb.render(sb)
        if (hb.hovered) {
            sb.setBlendFunction(770, 1)
            sb.color = Color(1.0f, 1.0f, 1.0f, 0.3f)
            sb.draw(
                ImageMaster.CHAR_OPT_HIGHLIGHT,
                x + 40.0f,
                y - 64.0f,
                64.0f,
                64.0f,
                300.0f,
                100.0f,
                Settings.scale,
                Settings.scale,
                0.0f,
                0,
                0,
                256,
                256,
                false,
                false
            )

            FontHelper.renderSmartText(
                sb, FontHelper.buttonLabelFont,
                label, x + 150.0f / 2, y + 20.0f, 200.0f, 25.0f, Settings.GOLD_COLOR
            )
            sb.setBlendFunction(770, 771)

        } else {
            FontHelper.renderSmartText(
                sb, FontHelper.buttonLabelFont,
                label, x + 150.0f / 2, y + 20.0f, 200.0f, 25.0f, Settings.CREAM_COLOR
            )
        }
    }

    override fun update() {
        hb.move(x + width, y)
        hb.update()
        if (!this.isHidden && hb.hovered) {
            if (InputHelper.justClickedLeft) {
                this.pressStarted = true
                onPress()
            } else if (InputHelper.isMouseDown) {
                onHold()
            } else if (InputHelper.justReleasedClickLeft) {
                onRelease()
                this.pressStarted = false
            } else if (InputHelper.justClickedRight) {
                this.pressStarted = true
                onRightClick()
            } else if (InputHelper.justReleasedClickRight) {
                onRightRelease()
                this.pressStarted = false
            }
        }
        if (!hb.hovered) this.pressStarted = false
    }
}