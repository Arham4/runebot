package com.gmail.arhamjsiddiqui.runebot

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.rowset.serial.SerialBlob


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
    var levels = IntArray(27)
        get() {
            return transaction {
                levelsBlob.getBytes(1, 27).toIntArray()
            }
        }
        set(value) {
            field = value
            transaction {
                levelsBlob = SerialBlob(value.toByteArray())
            }
        }
    var experiences = IntArray(27)
        get() {
            return transaction {
                experiencesBlob.getBytes(1, 27).toIntArray()
            }
        }
        set(value) {
            field = value
            transaction {
                experiencesBlob = SerialBlob(value.toByteArray())
            }
        }

    operator fun IntArray.set(index: Int, value: Int) {
        TODO("need to figure out how to make this override the existing set operator and update level")
    }

    private var levelsBlob by Players.levels
    private var experiencesBlob by Players.experiences

    private fun calculateCombat() {
        TODO("need to figure formula out")
    }
}
