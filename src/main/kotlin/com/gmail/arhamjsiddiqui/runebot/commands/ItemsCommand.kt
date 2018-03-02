package com.gmail.arhamjsiddiqui.runebot.commands

import com.gmail.arhamjsiddiqui.runebot.CommandFunctions
import com.gmail.arhamjsiddiqui.runebot.DiscordFunctions.queueMessage
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User

class ItemsCommand : CommandExecutor {

    @Command(aliases = ["r.items"], description = "Shows the items you have.")
    fun onItemsCommand(user: User, textChannel: TextChannel) {
        CommandFunctions.withPlayer(user, textChannel) { player ->
            var message = "Items in inventory:\n```\n"

            player.items.forEach { item ->
                message += "${item.count}x ${item.name}\n"
            }
            message += "```"
            textChannel.queueMessage(message)
        }
    }
}