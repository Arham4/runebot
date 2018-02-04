package com.gmail.arhamjsiddiqui.runebot

import com.gmail.arhamjsiddiqui.runebot.player.Player
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User

/**
 * A random assortment of functions (include extension functions here too)
 *
 * @author Arham 4
 */
object DatabaseFunctions {
    fun fetchPlayer(user: User): Player {
        return RuneBot.players[user] ?: Player(user)
    }
}
object CommandFunctions {
    inline fun withPlayer(user: User, textChannel: TextChannel, crossinline command: (player: Player) -> Unit) {
        val player = DatabaseFunctions.fetchPlayer(user)
        if (!player.cooldown.secondsPast(RuneBot.CONFIG.messageCooldown)) return
        player.textChannel = textChannel
        command.invoke(player)
        player.cooldown = System.currentTimeMillis()
    }
    fun Long.secondsPast(seconds: Int): Boolean = System.currentTimeMillis() - this >= (seconds * 1000)
}

fun String.asProperSubjectType(number: Int, plural: String = "${this}s") = if (number == 1) this else plural
fun TextChannel.queueMessage(message: String) = sendMessage(message).queue()
