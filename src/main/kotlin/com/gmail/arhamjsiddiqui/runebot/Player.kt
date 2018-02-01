package com.gmail.arhamjsiddiqui.runebot

import com.gmail.arhamjsiddiqui.runebot.jooq.tables.Players
import org.jooq.SQLDialect
import org.jooq.impl.DSL

/**
 * Represents a single player in RuneBot
 *
 * @author Arham 4
 */
class Player(val discordId: String) {
    var totalLevel: Int = 0
    var totalExp: Int = 0
    var exists = false

    init {
        val playerSQL = DSL.using(RuneBot.DATASOURCE, SQLDialect.POSTGRES)
                .select(Players.PLAYERS.DISCORD_ID)
                .from(Players.PLAYERS)
                .where(Players.PLAYERS.DISCORD_ID.eq(discordId))
                .fetchAny()
        if (playerSQL != null) {
            totalLevel = playerSQL.getValue(Players.PLAYERS.TOTAL_LEVEL, Int::class.java) // stuck!!
            totalExp = playerSQL.getValue(Players.PLAYERS.TOTAL_EXP, Int::class.java)
            exists = true
        }
    }
}