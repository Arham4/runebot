package com.gmail.arhamjsiddiqui.runebot.commands

import com.gmail.arhamjsiddiqui.runebot.CommandFunctions
import com.gmail.arhamjsiddiqui.runebot.DatabaseFunctions
import com.gmail.arhamjsiddiqui.runebot.DiscordFunctions.MarkdownText.bold
import com.gmail.arhamjsiddiqui.runebot.DiscordFunctions.queueMessage
import com.gmail.arhamjsiddiqui.runebot.RuneBot
import com.gmail.arhamjsiddiqui.runebot.data.SkillsData
import com.gmail.arhamjsiddiqui.runebot.entity.Player
import com.gmail.arhamjsiddiqui.runebot.mentionToId
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

    @Command(aliases = ["r.rank"], usage = "r.rank [Optional: SKILL] [Optional: @USER]", description = "Shows you or another player's current rank in the highscores of either a specific skill or in total level.")
    fun onRankCommand(args: Array<String>, theUser: User, textChannel: TextChannel) {
        onCommand(args, theUser, textChannel, { player ->
            val sortedList = RuneBot.players.map { it.value }.sortedWith(SkillsComparator())
            val rank = sortedList.indexOf(player) + 1
            textChannel.queueMessage("${player.asDiscordUser.asMention}'s total level rank: ${rank.toString().bold()}")
        }, { player ->
            val skillName = let {
                val input = args[0].toLowerCase()
                SkillsData.skills.skillNameForNickname[input] ?: input
            }
            val skillId = SkillsData.skills.skillIdFor[skillName]
            if (skillId != null) {
                val sortedList = RuneBot.players.map { it.value }.sortedWith(SkillsComparator(skillId))
                val rank = sortedList.indexOf(player) + 1
                textChannel.queueMessage("${player.asDiscordUser.asMention}'s ${SkillsData.skills.skillNameFor[skillId]!!.capitalize()} rank: ${rank.toString().bold()}")
            } else {
                TrainCommand.queueInvalidSkillNameMessage(textChannel, "r.rank")
            }
        })
    }

    @Command(aliases = ["r.highscore", "r.ranks"], usage = "r.highscore | r.ranks [Optional: SKILL]", description = "Shows the top 10 players in the highscores of either a specific skill or in total level.")
    fun onHighscoreCommand(args: Array<String>, theUser: User, textChannel: TextChannel) {
        onCommand(args, theUser, textChannel, { player ->
            val sortedList = RuneBot.players.map { it.value }.sortedWith(SkillsComparator()).subList(0, if (RuneBot.players.size > 10) 10 else RuneBot.players.size)
            var message = "Top 10 players by total level:\n```\n"
            message += "Name - Total Level - Total EXP\n"
            sortedList.forEachIndexed { index, rankedPlayer ->
                message += "${index + 1}. ${rankedPlayer.asDiscordUser.name} - Level: ${rankedPlayer.skills.totalLevel.zeroToOne()} - EXP: ${rankedPlayer.skills.totalExp}\n"
            }
            message += "```"
            textChannel.queueMessage(message)
        }, { player ->
            val skillName = let {
                val input = args[0].toLowerCase()
                SkillsData.skills.skillNameForNickname[input] ?: input
            }
            val skillId = SkillsData.skills.skillIdFor[skillName]
            if (skillId != null) {
                val sortedList = RuneBot.players.map { it.value }.sortedWith(SkillsComparator(skillId)).subList(0, if (RuneBot.players.size > 10) 10 else RuneBot.players.size)
                var message = "Top 10 players by ${SkillsData.skills.skillNameFor[skillId]!!.capitalize()}:\n```\n"
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
    private inline fun onCommand(args: Array<String>, theUser: User, textChannel: TextChannel, crossinline totalLevelAction: (player: Player) -> Unit, crossinline specificSkillAction: (player: Player) -> Unit) {
        var user = theUser
        if (!args.isEmpty() && args[0].isUser()) {
            user = RuneBot.BOT.getUserById(args[0].mentionToId)
        } else if (args.size > 1 && args[1].isUser()) {
            user = RuneBot.BOT.getUserById(args[1].mentionToId)
        }
        if (DatabaseFunctions.accountExists(user)) {
            CommandFunctions.withPlayer(user, textChannel) { player ->
                if (args.isEmpty() || args[0].isUser()) {
                    totalLevelAction(player)
                } else if (!args[0].isUser() || args[1].isUser()) {
                    specificSkillAction(player)
                }
            }
        }
    }

    private fun String.isUser(): Boolean {
        return this.startsWith("<@") && this.endsWith(">")
    }

    private fun Int.zeroToOne(): Int {
        return if (this == 0) 1 else this
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