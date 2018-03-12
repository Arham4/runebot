package com.gmail.arhamjsiddiqui.runebot.commands

import com.gmail.arhamjsiddiqui.runebot.DiscordFunctions.queueMessage
import com.gmail.arhamjsiddiqui.runebot.data.SkillsData
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dv8tion.jda.core.entities.TextChannel

/**
 * @author Arham 4
 */
class NicknamesCommand : CommandExecutor {

    @Command(aliases = ["r.nicknames"], description = "Shows the available nicknames for commands that use skills.")
    fun onNicknamesCommand(textChannel: TextChannel) {
        var message = "The available nicknames for skills are:\n```\n"
        SkillsData.skills.skillNameForNickname.forEach { nickname, skillName ->
            message += "$nickname - $skillName\n"
        }
        message += "```"
        textChannel.queueMessage(message)
    }
}