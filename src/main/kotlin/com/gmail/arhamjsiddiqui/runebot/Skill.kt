package com.gmail.arhamjsiddiqui.runebot

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

/**
 * Purpose of class...
 *
 * @author Arham 4
 */
class Skill(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Skill>(Skills)

    val skillId by Skills.skillId
    val experience by Skills.experience
}


object Skills : IntIdTable() {
    val skillId = integer("level")
    val experience = long("experience")
}