package com.gmail.arhamjsiddiqui.runebot

import com.gmail.arhamjsiddiqui.runebot.jooq.tables.Players
import main.kotlin.com.gmail.arhamjsiddiqui.runebot.player.Skills
import org.jooq.SQLDialect
import org.jooq.util.postgres.PostgresDSL

/**
 * Represents a single player in RuneBot
 *
 * @author Arham 4
 */
class Player(val discordId: String) {
    val skills: Skills = Skills()
    var exists = false

    init {
        val playerSQL = PostgresDSL.using(RuneBot.DATASOURCE, SQLDialect.POSTGRES)
                .selectFrom(Players.PLAYERS)
                .where(Players.PLAYERS.DISCORD_ID.eq(discordId))
                .fetchAny()
        if (playerSQL != null) {
            skills.totalLevel = playerSQL.totalLevel
            skills.totalExp = playerSQL.totalExp
            skills.levels = playerSQL.levels // stuck!!
            skills.experiences = playerSQL.experiences
            exists = true
        }
    }
}