package com.gmail.arhamjsiddiqui.runebot

import com.gmail.arhamjsiddiqui.runebot.jooq.tables.Players
import com.gmail.arhamjsiddiqui.runebot.player.Player
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
        val player = Player(user)
        if (player.exists) RuneBot.players.put(user, player)
        return if (player.exists) player else null
    }

    private fun makePlayer(user: User): Player {
        DSL.using(RuneBot.DATASOURCE, SQLDialect.POSTGRES).insertInto(Players.PLAYERS)
                .set(Players.PLAYERS.DISCORD_ID, user.id)
                .set(Players.PLAYERS.TOTAL_LEVEL, 0)
                .set(Players.PLAYERS.TOTAL_EXP, 0)
                .set(Players.PLAYERS.LEVELS, Array(25, {0}))
                .set(Players.PLAYERS.EXPERIENCES, Array(25, {0}))
                .execute()
        val player = Player(user)
        RuneBot.players.put(user, player)
        RuneBot.BOT.sendMessage("Welcome to RuneBot ${user.asMention}! Your account has successfully been created!")
        return player
    }
}

