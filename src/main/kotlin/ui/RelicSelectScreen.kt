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

    private fun open() {
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.NONE) AbstractDungeon.previousScreen =
            AbstractDungeon.screen

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