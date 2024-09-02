package action

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.vfx.AbstractGameEffect

class EmptyEffect(val cb: () -> Unit = {}) : AbstractGameEffect() {
    override fun dispose() {

    }

    override fun update() {
        cb()
        isDone = true
    }

    override fun render(p0: SpriteBatch?) {
    }
}