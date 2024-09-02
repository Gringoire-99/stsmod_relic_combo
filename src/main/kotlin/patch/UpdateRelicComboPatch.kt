package patch

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.rooms.AbstractRoom
import combo.AbstractRelicCombo

class UpdateRelicComboPatch {
    @SpirePatch2(clz = AbstractRelic::class, method = "obtain")
    internal class obtain {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun post(__instance: AbstractRelic?) {
                AbstractRelicCombo.updateCombo()
                __instance?.apply {
                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                        if (k.isActive(v)) {
                            k.onObtainRelic(this, v)
                        }
                    }
                }
            }
        }
    }


    @SpirePatch2(clz = AbstractRelic::class, method = "instantObtain", paramtypez = [])
    internal class instantObtain {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun post(__instance: AbstractRelic?) {
                AbstractRelicCombo.updateCombo()
                __instance?.apply {
                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                        if (k.isActive(v)) {
                            k.onObtainRelic(this, v)
                        }
                    }
                }
            }
        }
    }

    @SpirePatch2(
        clz = AbstractRelic::class,
        method = "instantObtain",
        paramtypez = [AbstractPlayer::class, Int::class, Boolean::class]
    )
    internal class instantObtain2 {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun post(__instance: AbstractRelic?) {
                AbstractRelicCombo.updateCombo()
                __instance?.apply {
                    AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                        if (k.isActive(v)) {
                            k.onObtainRelic(this, v)
                        }
                    }
                }
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer::class, method = "loseRelic")
    internal class loseRelic {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun post() {
                AbstractRelicCombo.updateCombo()
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer::class, method = "reorganizeRelics")
    internal class reorganizeRelics {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun post() {
                AbstractRelicCombo.updateCombo()
            }
        }
    }

    @SpirePatch2(clz = AbstractDungeon::class, method = "loadSave")
    internal class loadCombo {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun post() {
                AbstractRelicCombo.currentComboSet.clear()
                AbstractRelicCombo.updateCombo()
            }
        }
    }
}
