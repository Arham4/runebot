package com.gmail.arhamjsiddiqui.runebot.player

import com.gmail.arhamjsiddiqui.runebot.RuneBot
import com.gmail.arhamjsiddiqui.runebot.jooq.tables.Players
import com.gmail.arhamjsiddiqui.runebot.jooq.tables.records.PlayersRecord
import com.gmail.arhamjsiddiqui.runebot.queueMessage
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User
import org.jooq.DSLContext
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

    val asDiscordUser = user
    var textChannel: TextChannel? = null
    var cooldown: Long = 0

    init {
        val playerSQL = selectPlayerSQL()?.fetchAny()
        if (playerSQL != null) {
            instantiateVariables(playerSQL)
        } else {
            makePlayer()
        }
        RuneBot.players.put(user, this)
    }

    private fun instantiateVariables(playerSQL: PlayersRecord) {
        skills.totalLevel = playerSQL.totalLevel
        skills.totalExp = playerSQL.totalExp
        skills.levels = playerSQL.levels
        skills.experiences = playerSQL.experiences
    }

    private fun makePlayer() {
        sql { dsl, table ->
            dsl.insertInto(table)
                    .set(table.DISCORD_ID, user.id)
                    .set(table.TOTAL_LEVEL, 0)
                    .set(table.TOTAL_EXP, 0)
                    .set(table.LEVELS, Array(25, {0}))
                    .set(table.EXPERIENCES, Array(25, {0}))
                    .execute()
        }
        instantiateVariables(selectPlayerSQL()!!.fetchAny())
        textChannel?.queueMessage("Welcome to RuneBot ${user.asMention}! Your account has successfully been created!")
    }

    /**
     * Used to provide an easy-to-use DSLContext and table reference to make typing less static.
     */
    fun <K> sql(query: (dsl: DSLContext, table: Players) -> K): K {
        return query(DSL.using(RuneBot.DATASOURCE, SQLDialect.POSTGRES), Players.PLAYERS)
    }

    /**
     * This function is nullable due to the fact that the player might not be found.
     */
    fun selectPlayerSQL(): SelectConditionStep<PlayersRecord>? {
        return sql { dsl, table -> dsl.selectFrom(table).where(table.DISCORD_ID.eq(user.id)) }
    }
}