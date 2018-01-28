package com.gmail.arhamjsiddiqui.runebot

import net.dv8tion.jda.core.entities.User
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Miscellaneous but integral functions
 *
 * @author Arham 4
 */
object DatabaseFunctions {
    fun fetchPlayer(user: User): Player {
        var player = RuneBot.players[user]// first check if player is in our premade hashmap
        return player ?: transaction {
            if (player == null) player = fetchPlayerByDatabase(user) // if it's not there, get it from the database.
            player ?: makePlayer(user) // else, make the player
        }
    }

    private fun fetchPlayerByDatabase(user: User): Player? {
        val player = Player.find { Players.discordId eq user.id }.firstOrNull()
        if (player != null) RuneBot.players.put(user, player) // while getting it from the database, we add it to the hashmap.
        return player
    }

    private fun makePlayer(user: User): Player {
        val player = Player.new {
            username = user.name
            discordId = user.id
        }
        RuneBot.players.put(user, player) // after making the player, we add them to the hashmap
        RuneBot.BOT.sendMessage("Welcome to RuneBot ${user.asMention}! Your account has successfully been created!")
        return player
    }
}
fun IntArray.toByteArray(): ByteArray {
    return foldIndexed(ByteArray(size)) { i, a, v -> a.apply { set(i, v.toByte()) } }
}
fun ByteArray.toIntArray(): IntArray {
    return foldIndexed(IntArray(size)) { i, a, v -> a.apply { set(i, v) } }
}