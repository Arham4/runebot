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

object DiscordFunctions {
    /**
     * Extension functions for common functions in JDA.
     *
     * @author Arham 4
     */
    fun TextChannel.queueMessage(message: String) = sendMessage(message).queue()

    fun TextChannel.queueSimpleEmbedMessage(title: String, color: Color, message: String, image: String? = null) {
        val eb = EmbedBuilder()
        eb.setTitle(title, null)
        eb.setColor(color)
        eb.setDescription(message)
        eb.setThumbnail(image)
        sendMessage(eb.build()).queue()
    }

    fun TextChannel.queueEmbedMessage(builder: (embedBuilder: EmbedBuilder) -> Unit) {
        val eb = EmbedBuilder()
        builder(eb)
        sendMessage(eb.build()).queue()
    }

    object MarkdownText {
        fun String.italics() = "*$this*"
        fun String.bold() = "**$this**"
        fun String.boldItalics() = "***$this***"
        fun String.underline() = "__${this}__"
        fun String.underlineItalics() = "__*$this*__"
        fun String.underlineBold() = "__**$this**__"
        fun String.underlineBoldItalics() = "__***$this***__"
        fun String.strikethrough() = "~~$this~~"
        fun String.singleCodeBlock() = "`$this`"
        fun String.multiLineCodeBlock() = "```$this```"
        fun String.multiLineCodeBlock(language: String) = "```$language\n$this```"
    }
}

fun String.asProperSubjectType(number: Int, plural: String = "${this}s") = if (number == 1) this else plural
val String.withIndefinitePronoun: String
    get() {
        val firstLetter = first().toString()
        return if (firstLetter.matches(Regex("[aeiou]"))) "an $this" else "a $this"
    }