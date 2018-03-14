package com.gmail.arhamjsiddiqui.runebot.commands

import com.gmail.arhamjsiddiqui.runebot.*
import com.gmail.arhamjsiddiqui.runebot.DiscordFunctions.MarkdownText.bold
import com.gmail.arhamjsiddiqui.runebot.DiscordFunctions.queueMessage
import com.gmail.arhamjsiddiqui.runebot.data.SkillsData
import com.gmail.arhamjsiddiqui.runebot.entity.Player
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User

/**
 * All the commands relating to highscores.
 *
 * @author Arham 4
 */
class HighscoresCommands : CommandExecutor {

    @Command(aliases = ["r.rank"], usage = "r.rank [Optional: \"global\"] [Optional: SKILL] [Optional: @USER]", description = "Shows you or another player's current rank in the highscores of either a specific skill or in total level.")
    fun onRankCommand(args: Array<String>, theUser: User, textChannel: TextChannel) {
        onCommand(args, theUser, textChannel, { player, global ->
            val sortedList = RuneBot.players.filterKeys { if (!global) textChannel.guild.isMember(it) else true }.map { it.value }.sortedWith(SkillsComparator())
            val rank = sortedList.indexOf(player) + 1
            textChannel.queueMessage("${player.asDiscordUser.asMention}'s total level rank${if (global) " globally" else ""}: ${rank.toString().bold()}")
        }, { player, global ->
            val skillName = let {
                val input = if (!global) args[0].toLowerCase() else args[1].toLowerCase()
                SkillsData.skills.skillNameForNickname[input] ?: input
            }
            val skillId = SkillsData.skills.skillIdFor[skillName]
            if (skillId != null) {
                val sortedList = RuneBot.players.filterKeys { if (!global) textChannel.guild.isMember(it) else true }.map { it.value }.sortedWith(SkillsComparator(skillId))
                val rank = sortedList.indexOf(player) + 1
                textChannel.queueMessage("${player.asDiscordUser.asMention}'s ${SkillsData.skills.skillNameFor[skillId]!!.capitalize()} rank${if (global) " globally" else ""}: ${rank.toString().bold()}")
            } else {
                TrainCommand.queueInvalidSkillNameMessage(textChannel, "r.rank")
            }
        })
    }

    @Command(aliases = ["r.highscore", "r.ranks"], usage = "r.highscore | r.ranks [Optional: \"global\"][Optional: SKILL]", description = "Shows the top 10 players in the highscores of either a specific skill or in total level.")
    fun onHighscoreCommand(args: Array<String>, theUser: User, textChannel: TextChannel) {
        onCommand(args, theUser, textChannel, { player, global ->
            var sortedList = RuneBot.players.filterKeys { if (!global) textChannel.guild.isMember(it) else true }.map { it.value }.sortedWith(SkillsComparator())
            sortedList = sortedList.subList(0, if (sortedList.size > 10) 10 else sortedList.size)
            var message = "Top 10 players by total level${if (global) " globally" else ""}:\n```\n"
            message += "Name - Total Level - Total EXP\n"
            sortedList.forEachIndexed { index, rankedPlayer ->
                message += "${index + 1}. ${rankedPlayer.asDiscordUser.name} - Level: ${rankedPlayer.skills.totalLevel.zeroToOne()} - EXP: ${rankedPlayer.skills.totalExp}\n"
            }
            message += "```"
            textChannel.queueMessage(message)
        }, { player, global ->
            val skillName = let {
                val input = if (!global) args[0].toLowerCase() else args[1].toLowerCase()
                SkillsData.skills.skillNameForNickname[input] ?: input
            }
            val skillId = SkillsData.skills.skillIdFor[skillName]
            if (skillId != null) {
                var sortedList = RuneBot.players.filterKeys { if (!global) textChannel.guild.isMember(it) else true }.map { it.value }.sortedWith(SkillsComparator(skillId))
                sortedList = sortedList.subList(0, if (sortedList.size > 10) 10 else sortedList.size)
                var message = "Top 10 players by ${SkillsData.skills.skillNameFor[skillId]!!.capitalize()}${if (global) " globally" else ""}:\n```\n"
                message += "Name - Level - EXP\n"
                sortedList.forEachIndexed { index, rankedPlayer ->
                    message += "${index + 1}. ${rankedPlayer.asDiscordUser.name} - Level: ${rankedPlayer.skills.levels[skillId].zeroToOne()} - EXP: ${rankedPlayer.skills.experiences[skillId]}\n"
                }
                message += "```"
                textChannel.queueMessage(message)
            } else {
                TrainCommand.queueInvalidSkillNameMessage(textChannel, "r.highscore")
            }
        })
    }

    /**
     * Albeit this inline functions structure is that of onRankCommand, it will still work due to the nature of the
     * commands differences.
     */
    private inline fun onCommand(args: Array<String>, theUser: User, textChannel: TextChannel, crossinline totalLevelAction: (player: Player, global: Boolean) -> Unit, crossinline specificSkillAction: (player: Player, global: Boolean) -> Unit) {
        var user = theUser
        var global = false
        if (!args.isEmpty()) {
            if (args[0].isUser()) {
                user = RuneBot.BOT.getUserById(args[0].mentionToId)
            } else if (args[0].toLowerCase() == "global") {
                global = true
            }
        }
        if (args.size > 1 && args[1].isUser()) {
            user = RuneBot.BOT.getUserById(args[1].mentionToId)
        }
        if (args.size > 2 && args[2].isUser()) {
            user = RuneBot.BOT.getUserById(args[2].mentionToId)
        }
        if (DatabaseFunctions.accountExists(user)) {
            CommandFunctions.withPlayer(user, textChannel) { player ->
                if (args.isEmpty() || args.size == 1 || args.size == 2 && global && args[1].isUser()) {
                    totalLevelAction(player, global)
                } else if (args.size == 2 && !global && args[1].isUser() || args.size == 3 && args[2].isUser()) {
                    specificSkillAction(player, global)
                }
            }
        }
    }
}

private class SkillsComparator(private val skillId: Int = -1) : Comparator<Player> {
    override fun compare(o1: Player, o2: Player): Int {
        if (skillId != -1) {
            if (o1.skills.levels[skillId] > o2.skills.levels[skillId]) return -1
            else if (o1.skills.levels[skillId] < o2.skills.levels[skillId]) return 1
            else {
                if (o1.skills.experiences[skillId] < o2.skills.experiences[skillId]) return 1
                else if (o1.skills.experiences[skillId] > o2.skills.experiences[skillId]) return -1
            }
        } else {
            if (o1.skills.totalLevel > o2.skills.totalLevel) return -1
            else if (o1.skills.totalLevel < o2.skills.totalLevel) return 1
            else {
                if (o1.skills.totalExp > o2.skills.totalExp) return -1
                else if (o1.skills.totalExp < o2.skills.totalExp) return 1
            }
        }
        return 0
    }
}