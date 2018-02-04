package com.gmail.arhamjsiddiqui.runebot

import com.gmail.arhamjsiddiqui.runebot.commands.HelpCommand
import com.gmail.arhamjsiddiqui.runebot.commands.TrainCommand
import com.gmail.arhamjsiddiqui.runebot.player.Player
import com.gmail.arhamjsiddiqui.runebot.player.SkillsData
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import de.btobastian.sdcf4j.handler.JDA3Handler
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.User

/**
 * @author Arham 4
 */
object RuneBot {

    @JvmStatic
    fun main(args: Array<String>) {
    }

    init {
        /**
         * Load experiences before program starts.
         */
        SkillsData.experienceForLevel
    }

    /**
     * Contains all the players of RuneBot in a neat hashmap to make looking up faster.
     */
    val players = hashMapOf<User, Player>()

    val CONFIG: ConfigDto = YAMLParse.parseDto("config.yaml", ConfigDto::class)

    val BOT: JDA = let {
        fun registerListeners(registrants: () -> Unit) {
            registrants.invoke()
        }
        fun registerCommands(registrants: () -> Unit) {
            registrants.invoke()
        }

        val jda = JDABuilder(AccountType.BOT).setToken(CONFIG.discord.token).buildAsync()
        val cmd = JDA3Handler(jda)

        registerListeners {  }
        registerCommands {
            cmd.registerCommand(HelpCommand(cmd))
            cmd.registerCommand(TrainCommand())
        }

        jda
    }

    val DATASOURCE = let {
        val config = HikariConfig()

        config.jdbcUrl = CONFIG.jdbc.url
        config.username = CONFIG.jdbc.username
        config.password = CONFIG.jdbc.password
        config.isAutoCommit = true
        config.maximumPoolSize = 32

        HikariDataSource(config)
    }

    data class JDBCDto(val url: String, val driver: String, val username: String, val password: String)
    data class DiscordDto(val token: String)
    data class ConfigDto(val messageCooldown: Int, val jdbc: JDBCDto, val discord: DiscordDto)
}