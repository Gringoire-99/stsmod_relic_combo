package ui

import basemod.IUIElement
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.ImageMaster
import com.megacrit.cardcrawl.helpers.input.InputHelper

const val ArrowWidth = 48F

class NumberSelector(
    initialVal: Int = 0,
    val label: String = "",
    val range: IntRange = 0..0,
    private val xPos: Float,
    private val yPos: Float,
    private val layer: Int = 0,
    private val order: Int = 0,
    val onChange: (old: Int, new: Int) -> Unit = { _, _ -> }
) : IUIElement {
    private var leftArrowX: Float = xPos * Settings.xScale
    private var rightArrowX: Float = xPos * Settings.xScale + 100
    private var arrowY: Float = (yPos - 60) * Settings.yScale
    private var arrowWidth: Float = ArrowWidth * Settings.scale
    private var arrowHeight: Float = ArrowWidth * Settings.scale
    var value: Int = initialVal
        set(value) {
            if (value in range) {
                if (value != field) {
                    onChange(field, value)
                }
                field = value
            }
        }

    override fun render(sb: SpriteBatch?) {
        sb?.apply {
            FontHelper.renderFontLeft(
                sb,
                FontHelper.tipBodyFont,
                label,
                xPos * Settings.xScale,
                yPos * Settings.yScale,
                Settings.CREAM_COLOR
            )

            if (InputHelper.mX >= leftArrowX && InputHelper.mX <= leftArrowX + arrowWidth && InputHelper.mY >= arrowY && InputHelper.mY <= arrowY + arrowHeight) {
                sb.color = Color.WHITE
            } else {
                sb.color = Color.LIGHT_GRAY
            }
            if (value != range.first) {
                sb.draw(
                    ImageMaster.CF_LEFT_ARROW,
                    leftArrowX,
                    arrowY,
                    0f,
                    0f,
                    ArrowWidth,
                    ArrowWidth,
                    Settings.scale,
                    Settings.scale,
                    0f,
                    0,
                    0,
                    48,
                    48,
                    false,
                    false
                )
            }

            // Render the current biome amount
            FontHelper.renderFontLeft(
                sb,
                FontHelper.tipBodyFont,
                value.toString(),
                leftArrowX + 57 * Settings.xScale,
                arrowY + 24 * Settings.yScale,
                Settings.BLUE_TEXT_COLOR
            )
            // Render the right arrow
            if (InputHelper.mX >= rightArrowX && InputHelper.mX <= rightArrowX + arrowWidth && InputHelper.mY >= arrowY && InputHelper.mY <= arrowY + arrowHeight) {
                sb.color = Color.WHITE
            } else {
                sb.color = Color.LIGHT_GRAY
            }
            if (value != range.last) {
                sb.draw(
                    ImageMaster.CF_RIGHT_ARROW,
                    rightArrowX,
                    arrowY,
                    0f,
                    0f,
                    ArrowWidth,
                    ArrowWidth,
                    Settings.scale,
                    Settings.scale,
                    0f,
                    0,
                    0,
                    48,
                    48,
                    false,
                    false
                )
            }

        }
    }

    override fun update() {
        if (InputHelper.justClickedLeft) {
            if (InputHelper.mX >= leftArrowX && InputHelper.mX <= leftArrowX + arrowWidth && InputHelper.mY >= arrowY && InputHelper.mY <= arrowY + arrowHeight) {
                value--
            } else if (InputHelper.mX >= rightArrowX && InputHelper.mX <= rightArrowX + arrowWidth && InputHelper.mY >= arrowY && InputHelper.mY <= arrowY + arrowHeight) {
                value++
            }
        }
    }

    override fun renderLayer(): Int {
        return layer
    }

    override fun updateOrder(): Int {
        return order
    }
}