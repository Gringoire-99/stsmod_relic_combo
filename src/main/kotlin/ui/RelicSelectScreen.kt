package ui

import basemod.abstracts.CustomScreen
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.megacrit.cardcrawl.dungeons.AbstractDungeon

class RelicSelectScreen : CustomScreen() {
    companion object {
        @SpireEnum
        @JvmStatic
        lateinit var RelicSelectScreen: AbstractDungeon.CurrentScreen
    }


    override fun curScreen(): AbstractDungeon.CurrentScreen {
        return RelicSelectScreen
    }

    // Note that this can be private and take any parameters you want.
    // When you call openCustomScreen it finds the first method named "open"
    // and calls it with whatever arguments were passed to it.
    private fun open() {
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.NONE) AbstractDungeon.previousScreen =
            AbstractDungeon.screen
        // Call reopen in this example because the basics of
        // setting the current screen are the same across both
        reopen()
    }

    override fun reopen() {
        AbstractDungeon.screen = curScreen()
        AbstractDungeon.isScreenUp = true
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun render(p0: SpriteBatch?) {
        TODO("Not yet implemented")
    }

    override fun openingSettings() {

    }
}