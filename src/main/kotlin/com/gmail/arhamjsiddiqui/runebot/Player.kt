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
    private var levels = IntArray(25)
        get() {
            return transaction {
                levelsBlob.getBytes(1, 25).toIntArray()
            }
        }
        set(value) {
            field = value
            transaction {
                levelsBlob = SerialBlob(value.toByteArray())
            }
        }
    private var experiences = DoubleArray(25)

    private var levelsBlob by Players.levels
    private var experiencesBlob by Players.experiences

    private val experienceForLevel by lazy {
        val experienceForLevel: IntArray = intArrayOf()
        var points = 0
        var output = 0
        for (level in 1..99) {
            experienceForLevel[level] = output
            points += Math.floor(level + 300 * Math.pow(2.0, level / 7.0)).toInt()
            output = Math.floor((points / 4).toDouble()).toInt()
        }
        experienceForLevel
    }

    internal fun calculateLevel(skillId: Int) {
        levels[skillId] = getLevelForExperience(experiences[skillId])
    }

    private fun getExperienceForLevel(level: Int): Int {
        check(level in 1..99)
        return experienceForLevel[level]
    }

    private fun getLevelForExperience(experience: Double): Int {
        check(experience in 0.0..2.0E8)
        return (1..98).firstOrNull { experience < experienceForLevel[it + 1] } ?: 99
    }

    fun addExperience(skillId: Int, experience: Double) {
        check(skillId in 0..24 && experience in 0.0..2.0E8)
        experiences[skillId] += experience
        transaction { experiencesBlob = SerialBlob(experiences.toByteArray()) }
    }

    fun getExperience(skillId: Int): Double {
        check(skillId in 0..24)
        transaction { experiences = experiencesBlob.getBytes(1, 27).toDoubleArray() }
        return experiences[skillId]
    }

    private fun calculateCombat() {
        TODO("need to figure formula out")
    }

}
