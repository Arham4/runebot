package com.gmail.arhamjsiddiqui.runebot

import com.gmail.arhamjsiddiqui.runebot.player.Player
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
fun String.asProperSubjectType(number: Int) = if (number == 1) this else "${this}s"