package utils

import Core
import action.EmptyAction
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KClass

const val modId = "RelicCombo"
val logger: Logger = LogManager.getLogger(Core.Companion::class.java.name)
fun String.makeId(): String {
    return "${modId}:${this}"
}

fun KClass<*>.makeId(): String {
    return "$modId:${this.simpleName}"
}

fun addToBot(action: AbstractGameAction? = null, cb: () -> Unit = {}) {
    val a = action ?: EmptyAction { cb() }
    AbstractDungeon.actionManager.addToBottom(a)
}

fun addToTop(action: AbstractGameAction? = null, cb: () -> Unit = {}) {
    val a = action ?: EmptyAction { cb() }
    AbstractDungeon.actionManager.addToTop(a)
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