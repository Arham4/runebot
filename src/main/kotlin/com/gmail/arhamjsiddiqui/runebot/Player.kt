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
        val playerSQL = DSL.using(SQLDialect.POSTGRES)
                .selectFrom(Players.PLAYERS)
                .where(Players.PLAYERS.DISCORD_ID.eq(discordId))
                .fetchAny() // stuck!!
        if (playerSQL != null) {
            totalLevel = playerSQL.getValue(Players.PLAYERS.TOTAL_LEVEL)
            totalExp = playerSQL.getValue(Players.PLAYERS.TOTAL_EXP)
            exists = true
        }
    }
}