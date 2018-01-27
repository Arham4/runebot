package com.gmail.arhamjsiddiqui.runebot

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Represents a singular player
 *
 * @author Arham 4
 */
class Player(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Player>(Players)

    var username by Players.username
    var discordId by Players.discordId
    var totalLevel by Players.totalLevel
    var totalExp by Players.totalExp
    var combatLevel by Players.combatLevel
    var combatExp: Long by Players.combatExp

    private fun calculateCombat() {

    }

    /**
     * TODO Need to figure out a way to override the delegate and keep it's features while still being able to do these things every time it is set.
     */
    fun addCombatExp(combatExp: Long) {
        transaction {
            this@Player.combatExp += combatExp
            this@Player.totalExp += combatExp
        }
        calculateCombat()
    }
}