package com.gmail.arhamjsiddiqui.runebot.commands

import com.gmail.arhamjsiddiqui.runebot.DatabaseFunctions
import com.gmail.arhamjsiddiqui.runebot.RuneBot
import com.gmail.arhamjsiddiqui.runebot.sendMessage
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dv8tion.jda.core.entities.User
import java.util.concurrent.ThreadLocalRandom

/**
 * The r.train command to train your RuneBot player!
 *
 * @author Arham 4
 */
class TrainCommand : CommandExecutor {

    @Command(aliases = ["r.train"], description = "Trains your RuneBot player!")
    fun onTrainCommand(user: User, args: Array<String>) {
        val exp = ThreadLocalRandom.current().nextInt(0, 50)
        val player = DatabaseFunctions.fetchPlayer(user)
        player.totalExp += exp
        RuneBot.BOT.sendMessage("You have earned $exp EXP! Total EXP: " + player.totalExp)
    }
}