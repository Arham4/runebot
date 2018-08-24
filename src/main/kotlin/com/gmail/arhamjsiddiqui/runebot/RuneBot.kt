package com.gmail.arhamjsiddiqui.runebot

import com.gmail.arhamjsiddiqui.runebot.commands.*
import com.gmail.arhamjsiddiqui.runebot.data.CONFIG
import com.gmail.arhamjsiddiqui.runebot.data.SkillsData
import com.gmail.arhamjsiddiqui.runebot.entity.Player
import com.gmail.arhamjsiddiqui.runebot.jooq.tables.Players
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import de.btobastian.sdcf4j.handler.JDA3Handler
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.entities.User
import org.jooq.SQLDialect
import org.jooq.impl.DSL

/**
 * @author Arham 4
 */
object RuneBot {

    @JvmStatic
    fun main(args: Array<String>) {
        registerAllPlayers()
    }

    private fun registerAllPlayers() {
        DSL.using(RuneBot.DATASOURCE, SQLDialect.POSTGRES).selectFrom(Players.PLAYERS).forEach { record ->
            val user = RuneBot.BOT.getUserById(record.discordId)
            if (record.discordId != null && user != null && DatabaseFunctions.accountExists(user)) {
                players.put(user, DatabaseFunctions.fetchPlayer(user))
            }
        }
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
        fun JDA.registerListeners(registrants: JDA.() -> Unit) {
            registrants.invoke(this)
        }

        fun JDA3Handler.registerCommands(registrants: JDA3Handler.() -> Unit) {
            registrants.invoke(this)
        }

        val jda = JDABuilder(AccountType.BOT).setToken(CONFIG.discord.token).buildAsync()
        val cmd = JDA3Handler(jda)

        jda.registerListeners { }
        cmd.registerCommands {
            registerCommand(HelpCommand(cmd))
            registerCommand(NicknamesCommand())
            registerCommand(TrainCommand())
            registerCommand(ItemsCommand())
            registerCommand(SkillsCommand())
            registerCommand(HighscoresCommands())
        }

        jda.presence.game = Game.playing("r.help | r.commands")

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