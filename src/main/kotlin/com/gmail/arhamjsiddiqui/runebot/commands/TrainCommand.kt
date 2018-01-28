package com.gmail.arhamjsiddiqui.runebot.commands

import com.gmail.arhamjsiddiqui.runebot.DatabaseFunctions
import com.gmail.arhamjsiddiqui.runebot.RuneBot
import com.gmail.arhamjsiddiqui.runebot.sendMessage
import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import net.dv8tion.jda.core.entities.User
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.ThreadLocalRandom

/**
 * Class to train one's skills in RuneBot
 *
 * @author Arham 4
 */
class TrainCommand : CommandExecutor {
    @Command(aliases = ["r.train"], description = "Trains your RuneBot player!")
    fun onTrainCommand(user: User, args: Array<String>) {
        if (args.isEmpty()) {
            RuneBot.BOT.sendMessage("Incorrect usage! Use as: `r.train SKILL`")
            return
        }
        val player = DatabaseFunctions.fetchPlayer(user)
        val skillName = nicknameFor[args[0].toLowerCase()] ?: args[0].toLowerCase()
        if (skillName != "") {
            val skillId = skillIdFor[skillName]
            skillId?.let { transaction {
                val exp = String.format("%.2f", ThreadLocalRandom.current().nextDouble(0.0, 50.0))
                player.addExperience(skillId, exp.toDouble())
                RuneBot.BOT.sendMessage("You have earned $exp EXP for $skillName! Total EXP in $skillName: " + player.getExperience(skillId))
            } }
        }
    }

    /**
     * TODO transfer data to external files
     */

    private val nicknameFor = hashMapOf(
            Pair("atk", "attack"),
            Pair("str", "strength"),
            Pair("def", "defence"),
            Pair("rng", "range"),
            Pair("hp", "hitpoints"),
            Pair("mage", "magic"),
            Pair("pray", "prayer"),
            Pair("herb", "herblore"),
            Pair("rc", "runecrafting"),
            Pair("fm", "firemaking"),
            Pair("wc", "woodcutting"),
            Pair("slay", "slayer"),
            Pair("farm", "farming"),
            Pair("con", "construction"),
            Pair("hunt", "hunter"),
            Pair("sum", "summoning"),
            Pair("dg", "dungeoneering"),
            Pair("dung", "dungeoneering")
    )

    private val skillIdFor = hashMapOf(
            Pair("attack", 0),
            Pair("strength", 2),
            Pair("defence", 1),
            Pair("hitpoints", 3),
            Pair("range", 4),
            Pair("magic", 6),
            Pair("prayer", 5),
            Pair("agility", 16),
            Pair("herblore", 15),
            Pair("thieving", 17),
            Pair("crafting", 12),
            Pair("runecrafting", 20),
            Pair("mining", 14),
            Pair("smithing", 13),
            Pair("fishing", 10),
            Pair("cooking", 7),
            Pair("firemaking", 11),
            Pair("woodcutting", 8),
            Pair("fletching", 9),
            Pair("slayer", 18),
            Pair("farming", 19),
            Pair("construction", 22),
            Pair("hunter", 21),
            Pair("summoning", 23),
            Pair("dungeoneering", 24)
    )
}
