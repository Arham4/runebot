package com.gmail.arhamjsiddiqui.runebot.player

import com.gmail.arhamjsiddiqui.runebot.RuneBot
import com.gmail.arhamjsiddiqui.runebot.jooq.tables.Players
import com.gmail.arhamjsiddiqui.runebot.jooq.tables.records.PlayersRecord
import net.dv8tion.jda.core.entities.User
import org.jooq.SQLDialect
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL

/**
 * Represents a single player in RuneBot
 *
 * @author Arham 4
 */
class Player(private val user: User) {
    val skills: Skills = Skills(this)
    var exists = false

    /**
     * Allows us to use the player directly rather than always looking them up with their discord ID using DSL
     */
    val sql: SelectConditionStep<PlayersRecord>? by lazy {
        DSL.using(RuneBot.DATASOURCE, SQLDialect.POSTGRES).selectFrom(Players.PLAYERS).where(Players.PLAYERS.DISCORD_ID.eq(user.id))
    }

    val asDiscordUser = user

    init {
        val playerSQL = sql?.fetchAny()
        if (playerSQL != null) {
            skills.totalLevel = playerSQL.totalLevel
            skills.totalExp = playerSQL.totalExp
            skills.levels = playerSQL.levels
            skills.experiences = playerSQL.experiences
            exists = true
        }
    }
}