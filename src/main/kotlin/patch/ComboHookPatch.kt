package patch

import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.ui.panels.PotionPopUp
import combo.AbstractRelicCombo

class ComboHookPatch {
    @SpirePatch2(clz = AbstractPlayer::class, method = "applyStartOfCombatLogic")
    internal class StartOfCombat {
        companion object {
            @JvmStatic
            @SpirePostfixPatch
            fun postfix() {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onBattleStart(v)
                    }
                }
            }
        }
    }

    @SpirePatch2(clz = AbstractDungeon::class, method = "returnRandomPotion", paramtypez = [Boolean::class])
    internal class returnRandomPotion {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 1, localvars = ["roll"])
            fun insert(@ByRef roll: IntArray) {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onDropRandomPotion(roll, v)
                    }
                }
            }
        }
    }

    @SpirePatch2(clz = PotionPopUp::class, method = "updateTargetMode")
    internal class updateTargetMode {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 34)
            fun insert() {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onAfterUsePotion(v)
                    }
                }
            }
        }
    }

    @SpirePatch2(clz = PotionPopUp::class, method = "updateInput")
    internal class updateInput {
        companion object {
            @JvmStatic
            @SpireInsertPatch(rloc = 23)
            fun insert() {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v)) {
                        k.onAfterUsePotion(v)
                    }
                }
            }
        }
    }

    @SpirePatch2(clz = ApplyPowerAction::class, method = "update")
    internal class applyPower {
        companion object {
            @JvmStatic
            @SpirePrefixPatch
            fun prefix(
                ___target: AbstractCreature?,
                ___source: AbstractCreature?,
                ___powerToApply: AbstractPower?,
                @ByRef ___startingDuration: FloatArray,
                @ByRef ___duration: FloatArray
            ) {
                AbstractRelicCombo.currentComboSet.forEach { (k, v) ->
                    if (k.isActive(v) && ___duration[0] == ___startingDuration[0]) {
                        val shouldContinue =
                            k.onApplyPower(v, target = ___target, source = ___source, power = ___powerToApply)
                        if (!shouldContinue) {
                            ___duration[0] = ___duration[0] - Gdx.graphics.deltaTime
                            SpireReturn.Return()
                        }
                    }
                }
            }
        }
    }
}