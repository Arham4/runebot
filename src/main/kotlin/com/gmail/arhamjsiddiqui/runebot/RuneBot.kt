package com.gmail.arhamjsiddiqui.runebot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.gmail.arhamjsiddiqui.runebot.commands.TrainCommand
import de.btobastian.sdcf4j.handler.JDA3Handler
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.entities.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.FileSystems
import java.nio.file.Files

/**
 * @author Arham 4
 */
object RuneBot {

    @JvmStatic
    fun main(args: Array<String>) {
        Database.connect(config.jdbc.url, config.jdbc.driver, config.jdbc.user, config.jdbc.password)
        transaction {
            logger.addLogger(StdOutSqlLogger)
            create(Players)
        }
        BOT
    }

    private val config: ConfigDto = let {
        val fileName = "config.yaml"
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule())

        Files.newBufferedReader(FileSystems.getDefault().getPath(fileName)).use { mapper.readValue(it, ConfigDto::class.java) }
    }

    val GUILD_ID = config.guildId
    val players = hashMapOf<User, Player>()

    val BOT: JDA = let {
        fun registerListeners(registrants: () -> Unit) {
            registrants.invoke()
        }
        fun registerCommands(registrants: () -> Unit) {
            registrants.invoke()
        }

        val jda = JDABuilder(AccountType.BOT).setToken(config.token).buildAsync()
        val cmd = JDA3Handler(jda)

        registerCommands {
            cmd.registerCommand(TrainCommand())
        }

        jda
    }

    private data class JDBCDto(val url: String, val driver: String, val user: String, val password: String)
    private data class ConfigDto(val jdbc: JDBCDto, val token: String, val guildId: Long)
}