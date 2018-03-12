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
import java.awt.Color
import java.awt.Font
import java.io.File
import java.nio.file.Files
import java.util.*
import javax.imageio.ImageIO


class SkillsCommand : CommandExecutor {

    @Command(aliases = ["r.skills"], async = true, usage = "r.skills [Optional: @USER]", description = "Shows the stats you or another player have.")
    fun onItemsCommand(args: Array<String>, theUser: User, textChannel: TextChannel) {
        var user = theUser
        if (!args.isEmpty()) {
            user = RuneBot.BOT.getUserById(args[0].mentionToId)
        }
        if (DatabaseFunctions.accountExists(user)) {
            CommandFunctions.withPlayer(user, textChannel) { player ->
                val template = File("./data/images/blank_skills.png")
                val newFile = File("./data/images/skills_${user.name}.png")
                val fontName = "RuneScape UF"

                val image = ImageIO.read(template)
                val graphics = image.graphics
                graphics.color = Color(0xFF, 0x8C, 0x00)
                graphics.font = Font(fontName, Font.PLAIN, 20)
                var indices = arrayOf(0, 2, 1, 4, 5, 6, 20, 22, 24)
                repeat(9) { i ->
                    graphics.drawString("${player.skills.levels[indices[i]].zeroToOne()}", 60, 61 + (i * (29)) - i)
                }
                indices = arrayOf(3, 16, 15, 17, 12, 9, 18, 21)
                repeat(8) { i ->
                    graphics.drawString("${player.skills.levels[indices[i]].zeroToOne()}", 125, 61 + (i * (29)) - i)
                }
                indices = arrayOf(14, 13, 10, 7, 11, 8, 19, 23)
                repeat(8) { i ->
                    graphics.drawString("${player.skills.levels[indices[i]].zeroToOne()}", 190, 61 + (i * (29)) - i)
                }
                graphics.font = Font(fontName, Font.PLAIN, 14)
                graphics.drawString("${player.skills.totalLevel}", 174, 286)
                graphics.dispose()

                ImageIO.write(image, "png", newFile)
                textChannel.queueMessage("${player.asDiscordUser.asMention}'s stats:")
                textChannel.sendFile(newFile).queue()
                /**
                 * Delete the file afterwards to conserve disk space
                 */
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        Files.delete(newFile.toPath())
                    }
                }, 3000)
            }
        } else {
            textChannel.queueMessage("Unable to fetch skills. User is not registered.")
        }
    }

    private fun Int.zeroToOne(): Int {
        return if (this == 0) 1 else this
    }
}