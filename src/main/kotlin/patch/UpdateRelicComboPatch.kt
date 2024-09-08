package patch

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.AbstractRelic
import core.AbstractRelicCombo
import core.PatchEffect
import utils.toLog

class UpdateRelicComboPatch {
    @SpirePatch2(clz = AbstractRelic::class, method = "obtain")
    internal class obtain {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun post(__instance: AbstractRelic?) {
                __instance?.apply {
                    PatchEffect.onPostObtainRelicSubscribers.forEach { it.effect(this) }
                }
                AbstractRelicCombo.updateCombo()
            }
        }
    }


    @SpirePatch2(clz = AbstractRelic::class, method = "instantObtain", paramtypez = [])
    internal class instantObtain {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun post(__instance: AbstractRelic?) {
                __instance?.apply {
                    PatchEffect.onPostObtainRelicSubscribers.forEach { it.effect(this) }
                }
                AbstractRelicCombo.updateCombo()
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
                __instance?.apply {
                    PatchEffect.onPostObtainRelicSubscribers.forEach { it.effect(this) }
                }
                AbstractRelicCombo.updateCombo()
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
}
