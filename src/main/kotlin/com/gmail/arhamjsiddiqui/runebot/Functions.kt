package com.gmail.arhamjsiddiqui.runebot

import com.gmail.arhamjsiddiqui.runebot.data.CONFIG
import com.gmail.arhamjsiddiqui.runebot.entity.Player
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User
import java.awt.Color


/**
 * A random assortment of functions (include extension functions here too)
 *
 * @author Arham 4
 */
object DatabaseFunctions {
    fun fetchPlayer(user: User, textChannel: TextChannel? = null): Player {
        val player = RuneBot.players[user]
        player?.textChannel = textChannel
        return player ?: Player(user, textChannel)
    }
}
object CommandFunctions {
    inline fun withPlayer(user: User, textChannel: TextChannel, crossinline command: (player: Player) -> Unit) {
        val player = DatabaseFunctions.fetchPlayer(user, textChannel)
        if (!player.cooldown.secondsPast(CONFIG.messageCooldown)) return
        command.invoke(player)
        player.cooldown = System.currentTimeMillis()
    }
    fun Long.secondsPast(seconds: Int): Boolean = System.currentTimeMillis() - this >= (seconds * 1000)
}

fun String.asProperSubjectType(number: Int, plural: String = "${this}s") = if (number == 1) this else plural
val String.withIndefinitePronoun: String
    get() {
        val firstLetter = first().toString()
        return if (firstLetter.matches(Regex("[aeiou]"))) "an $this" else "a $this"
    }
fun TextChannel.queueMessage(message: String) = sendMessage(message).queue()
fun TextChannel.sendEmbedMessage(title: String, color: Color = Color(0x3b5262), message: String, image: String? = null) {
    val eb = EmbedBuilder()
    eb.setTitle(title, null)
    eb.setColor(color)
    eb.setDescription(message)
    eb.setThumbnail(image)
    sendMessage(eb.build()).queue()
}