package com.gmail.arhamjsiddiqui.runebot.commands

import com.gmail.arhamjsiddiqui.runebot.CommandFunctions
import com.gmail.arhamjsiddiqui.runebot.DatabaseFunctions
import com.gmail.arhamjsiddiqui.runebot.DiscordFunctions.queueMessage
import com.gmail.arhamjsiddiqui.runebot.RuneBot
import com.gmail.arhamjsiddiqui.runebot.mentionToId
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User

class ItemsCommand : CommandExecutor {

    @Command(aliases = ["r.items"], usage = "r.items [Optional: @USER]", description = "Shows the items you have.")
    fun onItemsCommand(args: Array<String>, theUser: User, textChannel: TextChannel) {
        var user = theUser
        if (!args.isEmpty()) {
            user = RuneBot.BOT.getUserById(args[0].mentionToId)
        }
        if (DatabaseFunctions.accountExists(user)) {
            CommandFunctions.withPlayer(user, textChannel) { player ->
                var message = "${player.asDiscordUser.asMention}'s inventory:\n```\n"

                player.items.forEach { item ->
                    message += "${item.count}x ${item.name}\n"
                }
                message += "```"
                textChannel.queueMessage(message)
            }
        } else {
            textChannel.queueMessage("Unable to fetch inventory. User is not registered.")
        }
    }
}