package utils

import RelicComboCore
import action.EmptyAction
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.actions.common.GainBlockAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.CardQueueItem
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KClass

const val modId = "RelicCombo"
val logger: Logger = LogManager.getLogger(RelicComboCore::class.java.name)
fun log(msg: String = "", vararg obj: Any?) {
    val joinToString = obj.joinToString(" + ") { it.toString() }
    logger.info("*******${msg} : $joinToString *******")
}

fun Any?.toLog(msg: String = "") {
    log(msg, this)
}

fun String.makeId(): String {
    return "${modId}:${this}"
}

fun KClass<*>.makeId(): String {
    return "$modId:${this.simpleName}"
}

fun addToBot(vararg action: AbstractGameAction = arrayOf(), cb: () -> Unit = {}) {
    val a = action.toMutableList()
    a.add(EmptyAction { cb() })
    a.forEach {
        AbstractDungeon.actionManager.addToBottom(it)
    }
}

fun addToTop(vararg action: AbstractGameAction = arrayOf(), cb: () -> Unit = {}) {
    val a: ArrayList<AbstractGameAction> = arrayListOf(EmptyAction { cb() })
    a.addAll(action)
    a.forEach {
        AbstractDungeon.actionManager.addToTop(it)
    }
}

fun dealDamage(
    p: AbstractCreature?,
    m: AbstractCreature?,
    damage: Int,
    damageInfo: DamageInfo = DamageInfo(
        p ?: AbstractDungeon.player, damage, DamageInfo.DamageType.NORMAL,
    ),
    damageEffect: AttackEffect = AttackEffect.SLASH_DIAGONAL,
    vfx: (target: AbstractCreature) -> Unit = {}
) {
    addToBot {
        val target = m ?: getRandomMonster()
        target?.let {
            AbstractDungeon.actionManager.addToTop(
                DamageAction(
                    it,
                    damageInfo,
                    damageEffect
                )
            )
            vfx(it)
        }
    }
}

fun gainBlock(c: AbstractCreature? = AbstractDungeon.player, b: Int) {
    AbstractDungeon.actionManager.addToTop(GainBlockAction(c, b))
}

fun getRandomMonster(): AbstractMonster? {
    return AbstractDungeon.getMonsters().getRandomMonster(true)
}

fun drawCard(number: Int) {
    AbstractDungeon.actionManager.addToBottom(DrawCardAction(number))
}

fun getAllGroup(): ArrayList<CardGroup> {
    return arrayListOf(
        AbstractDungeon.player.hand,
        AbstractDungeon.player.drawPile,
        AbstractDungeon.player.discardPile,
        AbstractDungeon.player.limbo, AbstractDungeon.player.exhaustPile
    )
}

fun AbstractCard.upDamage(amount: Int) {
    this.baseDamage += amount
    this.upgradedDamage = true
}

fun AbstractCard.upBlock(amount: Int) {
    this.baseBlock += amount
    this.upgradedBlock = true
}

fun AbstractCard.upMagicNumber(amount: Int) {
    this.baseMagicNumber += amount
    this.magicNumber = this.baseMagicNumber
    this.upgradedMagicNumber = true
}

fun isInCombat(): Boolean {
    return AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().monsters != null && !AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()
}

fun addToQueue(
    card: AbstractCard,
    t: AbstractCreature?,
    random: Boolean = false,
    purge: Boolean = false,
    cb: (AbstractCard) -> Unit = {}
) {
    val tmp = card.makeStatEquivalentCopy()
    if (purge) {
        AbstractDungeon.player.limbo.addToBottom(tmp)
        tmp.purgeOnUse = true
    }
    tmp.current_x = card.current_x
    tmp.current_y = card.current_y
    tmp.target_x = Settings.WIDTH.toFloat() / 2.0f - 300.0f * Settings.scale
    tmp.target_y = Settings.HEIGHT.toFloat() / 2.0f
    val m = if (t is AbstractMonster) t else null
    if (t is AbstractMonster) {
        tmp.calculateCardDamage(t)
    }
    if (random) {
        AbstractDungeon.actionManager.addCardQueueItem(
            CardQueueItem(tmp, true, card.energyOnUse, true, true), true
        )
    } else {
        AbstractDungeon.actionManager.addCardQueueItem(
            CardQueueItem(tmp, m, card.energyOnUse, true, true), true
        )
    }
    cb(tmp)
}

fun AbstractMonster.isAlive(): Boolean {
    return !this.halfDead && !this.isDying && !this.isEscaping
}

