package com.gmail.arhamjsiddiqui.runebot.data

data class JDBCDto(val url: String, val driver: String, val username: String, val password: String)
data class DiscordDto(val token: String)
data class ConfigDto(val messageCooldown: Int, val jdbc: JDBCDto, val discord: DiscordDto)

val CONFIG: ConfigDto = YAMLParse.parseDto("config.yaml", ConfigDto::class)