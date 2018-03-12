package com.gmail.arhamjsiddiqui.runebot

import com.gmail.arhamjsiddiqui.runebot.commands.HelpCommand
import com.gmail.arhamjsiddiqui.runebot.commands.ItemsCommand
import com.gmail.arhamjsiddiqui.runebot.commands.SkillsCommand
import com.gmail.arhamjsiddiqui.runebot.commands.TrainCommand
import com.gmail.arhamjsiddiqui.runebot.data.CONFIG
import com.gmail.arhamjsiddiqui.runebot.data.SkillsData
import com.gmail.arhamjsiddiqui.runebot.entity.Player
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import de.btobastian.sdcf4j.handler.JDA3Handler
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.entities.User
import java.awt.Font
import java.awt.GraphicsEnvironment
import java.io.File

/**
 * @author Arham 4
 */
object RuneBot {

    @JvmStatic
    fun main(args: Array<String>) {
        /**
         * Register font for writing skills on image with RuneScape font.
         */
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, File("./runescape_uf.ttf")))
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
            cmd.registerCommand(ItemsCommand())
            cmd.registerCommand(SkillsCommand())
        }

        jda.presence.game = Game.playing("r.help")

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
}