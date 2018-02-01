package com.gmail.arhamjsiddiqui.runebot

import com.gmail.arhamjsiddiqui.runebot.jooq.tables.Players
import net.dv8tion.jda.core.entities.User
import org.jooq.SQLDialect
import org.jooq.impl.DSL

/**
 * A random assortment of functions (include extension functions here too)
 *
 * @author Arham 4
 */
object DatabaseFunctions {
    fun fetchPlayer(user: User): Player {
        var player = RuneBot.players[user]
        return player ?: let {
            player = fetchPlayerByDatabase(user)
            player ?: makePlayer(user)
        }
    }

    /**
     * Need to make this method more efficient
     */
    private fun fetchPlayerByDatabase(user: User): Player? {
        val player = Player(user.id)
        return if (player.exists) player else null
    }

    private fun makePlayer(user: User): Player {
        DSL.using(SQLDialect.POSTGRES).insertInto(Players.PLAYERS)
                .set(Players.PLAYERS.DISCORD_ID, user.id)
                .set(Players.PLAYERS.TOTAL_LEVEL, 0)
                .set(Players.PLAYERS.TOTAL_EXP, 0)
                .execute()
        return Player(user.id)
    }
}