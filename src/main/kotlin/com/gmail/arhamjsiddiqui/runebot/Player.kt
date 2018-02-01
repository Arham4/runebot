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
                .selectFrom(Players.PLAYERS)
                .where(Players.PLAYERS.DISCORD_ID.eq(discordId))
                .fetchAny()
        if (playerSQL != null) {
            totalLevel = playerSQL.totalLevel
            totalExp = playerSQL.totalExp
            exists = true
        }
    }
}