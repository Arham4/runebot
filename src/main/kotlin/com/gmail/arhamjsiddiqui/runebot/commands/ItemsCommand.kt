package com.gmail.arhamjsiddiqui.runebot.commands

import com.gmail.arhamjsiddiqui.runebot.CommandFunctions
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User

class ItemsCommand : CommandExecutor {

    @Command(aliases = ["r.items"], description = "Shows the items you have.")
    fun onItemsCommand(user: User, textChannel: TextChannel): String {
        var message = "Items in inventory:\n```\n"
        CommandFunctions.withPlayer(user, textChannel) { player ->
            player.items.forEach { item ->
                message += "${item.count}x ${item.name}\n"
            }
        }
        message += "```"
        return message
    }
}