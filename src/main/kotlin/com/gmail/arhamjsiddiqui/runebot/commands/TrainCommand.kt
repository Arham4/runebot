package com.gmail.arhamjsiddiqui.runebot.commands

import com.gmail.arhamjsiddiqui.runebot.CommandFunctions.withPlayer
import com.gmail.arhamjsiddiqui.runebot.DiscordFunctions.queueMessage
import com.gmail.arhamjsiddiqui.runebot.data.SkillsData
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User
import java.util.concurrent.ThreadLocalRandom

/**
 * The r.train command to train your RuneBot player!
 *
 * @author Arham 4
 */
class TrainCommand : CommandExecutor {

    @Command(aliases = ["r.train"], usage = "r.train SKILL", description = "Trains your RuneBot player!")
    fun onTrainCommand(textChannel: TextChannel, user: User, args: Array<String>) {
        if (args.isEmpty()) {
            textChannel.queueMessage("Incorrect usage! Use as: `r.train SKILL`")
            return
        }
        val exp = ThreadLocalRandom.current().nextInt(1, 50)
        withPlayer(user, textChannel) { player ->
            val skillName = let {
                val input = args[0].toLowerCase()
                SkillsData.skills.skillNameForNickname[input] ?: input
            }
            val skillId = SkillsData.skills.skillIdFor[skillName]
            if (skillId != null) {
                player.train(skillId, exp)
                textChannel.queueMessage("You have earned $exp EXP! Total EXP in ${skillName.capitalize()}: "
                        + player.skills.experiences[skillId])
            } else {
                textChannel.queueMessage("Invalid skill name! Only the following can be used:\n```" +
                        "${SkillsData.skills.skillNameFor.toPresentableString()}```\nNicknames are available " +
                        "(for example: `r.train attack` can be `r.train atk`).")
            }
        }
    }

    private fun Map<Int, String>.toPresentableString(): String {
        var output = ""
        keys.forEach { output += "r.train ${this[it]}\n" }
        return output
    }
}