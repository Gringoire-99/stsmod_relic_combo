package action

import com.megacrit.cardcrawl.actions.AbstractGameAction

class EmptyAction(val effect: () -> Unit) : AbstractGameAction() {
    override fun update() {
        effect()
        isDone = true
    }
}