package com.gmail.arhamjsiddiqui.runebot

import org.jetbrains.exposed.dao.IntIdTable

/**
 * A file that contains multiple database tables.
 *
 * @author Arham 4
 */
object Players : IntIdTable() {
    val username = varchar("username", 32).index()
    val discordId = text("discord_id")
    val totalLevel = integer("total_level").default(0)
    val totalExp = long("total_exp").default(0)
    val levels = blob("levels")
    val experiences = blob("experiences")
}