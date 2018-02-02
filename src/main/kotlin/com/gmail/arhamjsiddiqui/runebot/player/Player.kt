package com.gmail.arhamjsiddiqui.runebot.player

import com.gmail.arhamjsiddiqui.runebot.RuneBot
import com.gmail.arhamjsiddiqui.runebot.jooq.tables.Players
import com.gmail.arhamjsiddiqui.runebot.jooq.tables.records.PlayersRecord
import com.gmail.arhamjsiddiqui.runebot.sendMessage
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
            instantiateVariables(playerSQL)
        } else {
            makePlayer()
        }
    }

    private fun instantiateVariables(playerSQL: PlayersRecord) {
        skills.totalLevel = playerSQL.totalLevel
        skills.totalExp = playerSQL.totalExp
        skills.levels = playerSQL.levels
        skills.experiences = playerSQL.experiences
    }

    private fun makePlayer() {
        DSL.using(RuneBot.DATASOURCE, SQLDialect.POSTGRES).insertInto(Players.PLAYERS)
                .set(Players.PLAYERS.DISCORD_ID, user.id)
                .set(Players.PLAYERS.TOTAL_LEVEL, 0)
                .set(Players.PLAYERS.TOTAL_EXP, 0)
                .set(Players.PLAYERS.LEVELS, Array(25, {0}))
                .set(Players.PLAYERS.EXPERIENCES, Array(25, {0}))
                .execute()
        instantiateVariables(sql!!.fetchAny()) // !! because we just added them above, so there is no way it would be null.
        RuneBot.players.put(user, this)
        RuneBot.BOT.sendMessage("Welcome to RuneBot ${user.asMention}! Your account has successfully been created!")
    }
}