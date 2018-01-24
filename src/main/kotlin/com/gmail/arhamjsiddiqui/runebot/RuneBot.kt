package com.gmail.arhamjsiddiqui.runebot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.jetbrains.exposed.sql.Database
import sx.blah.discord.api.ClientBuilder
import java.nio.file.FileSystems
import java.nio.file.Files

/**
 * @author Arham 4
 */
fun main(args: Array<String>) {
    Database.connect(config.jdbc.url, config.jdbc.driver, config.jdbc.user, config.jdbc.password)
    bot
}

private val config: ConfigDto
    get() {
        val fileName = "config.yaml"
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule())

        return Files.newBufferedReader(FileSystems.getDefault().getPath(fileName)).use { mapper.readValue(it, ConfigDto::class.java) }
    }

val bot by lazy {
    fun registerListeners(registrants: () -> Unit) {
        registrants.invoke()
    }

    fun registerCommands(registrants: () -> Unit) {
        registrants.invoke()
    }

    val token = config.token
    val clientBuilder = ClientBuilder()

    clientBuilder.withToken(token)
    clientBuilder.login()

    registerListeners {

    }

    registerCommands {

    }
}

private data class JDBCDto(val url: String, val driver: String, val user: String, val password: String)
private data class ConfigDto(val jdbc: JDBCDto, val token: String)