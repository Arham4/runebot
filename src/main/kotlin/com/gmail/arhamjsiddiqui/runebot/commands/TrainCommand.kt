package com.gmail.arhamjsiddiqui.runebot.commands

import com.gmail.arhamjsiddiqui.runebot.Functions
import com.gmail.arhamjsiddiqui.runebot.RuneBot
import com.gmail.arhamjsiddiqui.runebot.sendMessage
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dv8tion.jda.core.entities.User
import java.util.concurrent.ThreadLocalRandom

/**
 * Class to train one's skills in RuneBot
 *
 * @author Arham 4
 */
class TrainCommand : CommandExecutor {
    @Command(aliases = ["r.train"], description = "Trains your RuneBot player!")
    fun onTrainCommand(user: User) {
        val exp = ThreadLocalRandom.current().nextLong(0, 50)
        val player = Functions.fetchPlayer(user)
        player.addCombatExp(exp)
        RuneBot.BOT.sendMessage("You have earned $exp EXP! Total EXP: " + player.totalExp)
    }
}