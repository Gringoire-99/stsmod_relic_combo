package ui

import com.megacrit.cardcrawl.helpers.PowerTip

class ComboPowerTip(
    header: String,
    body: String,
    val id: String
) : PowerTip(header, body) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComboPowerTip

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}