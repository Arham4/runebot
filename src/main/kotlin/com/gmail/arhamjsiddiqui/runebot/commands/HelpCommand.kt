package com.gmail.arhamjsiddiqui.runebot.commands

import de.btobastian.sdcf4j.Command
import de.btobastian.sdcf4j.CommandExecutor
import de.btobastian.sdcf4j.CommandHandler

class HelpCommand(private val commandHandler: CommandHandler) : CommandExecutor {

    @Command(aliases = ["r.help", "r.commands"], description = "Displays this page.")
    fun onHelpCommand(): String {
        val builder = StringBuilder()
        builder.append("```xml")
        for (simpleCommand in commandHandler.commands) {
            if (!simpleCommand.commandAnnotation.showInHelpPage) {
                continue
            }
            builder.append("\n")
            if (!simpleCommand.commandAnnotation.requiresMention) {
                builder.append(commandHandler.defaultPrefix)
            }
            var usage = simpleCommand.commandAnnotation.usage
            if (usage.isEmpty()) {
                usage = simpleCommand.commandAnnotation.aliases[0]
            }
            builder.append(usage)
            val description = simpleCommand.commandAnnotation.description
            if (description != "none") {
                builder.append(" | ").append(description)
            }
        }
        builder.append("\n```")
        return builder.toString()
    }

}