package com.gmail.arhamjsiddiqui.runebot.entity

import com.gmail.arhamjsiddiqui.runebot.DiscordFunctions.MarkdownText.bold
import com.gmail.arhamjsiddiqui.runebot.DiscordFunctions.queueMessage
import com.gmail.arhamjsiddiqui.runebot.DiscordFunctions.queueSimpleEmbedMessage
import com.gmail.arhamjsiddiqui.runebot.ItemFunctions
import com.gmail.arhamjsiddiqui.runebot.RuneBot
import com.gmail.arhamjsiddiqui.runebot.data.SkillsData
import com.gmail.arhamjsiddiqui.runebot.ifPercentage
import com.gmail.arhamjsiddiqui.runebot.jooq.tables.Players
import com.gmail.arhamjsiddiqui.runebot.jooq.tables.records.PlayersRecord
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.SelectConditionStep
import org.jooq.impl.DSL
import java.awt.Color

/**
 * Represents a single player in RuneBot
 *
 * @author Arham 4
 */
class Player(private val user: User, var textChannel: TextChannel? = null) {
    val skills: Skills = Skills(this)
    val items: ArrayList<Item> = arrayListOf()

    val asDiscordUser = user
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
        playerSQL.itemIds?.forEachIndexed { index, itemId ->
            val item = Item(itemId, playerSQL.itemCounts[index])
            items.add(item)
        }
    }

    private fun makePlayer() {
        sql { dsl, table ->
            dsl.insertInto(table)
                    .set(table.DISCORD_ID, user.id)
                    .set(table.TOTAL_LEVEL, 0)
                    .set(table.TOTAL_EXP, 0)
                    .set(table.LEVELS, Array(25, {0}))
                    .set(table.EXPERIENCES, Array(25, {0}))
                    .set(table.ITEM_IDS, Array(0, {0}))
                    .set(table.ITEM_COUNTS, Array(0, {0}))
                    .execute()
        }
        instantiateVariables(selectPlayerSQL()!!.fetchAny())
        skills.addExperience(3, 1154)
        textChannel?.queueMessage("Welcome to RuneBot ${user.asMention}! Your account has successfully been created!")
    }

    fun train(skillId: Int, exp: Int) {
        skills.addExperience(skillId, exp)
        ifPercentage(10) {
            val item = ItemFunctions.generateRandomItem()
            val message = let {
                val baseMessage = "You've received a ${item.definition.name}!"
                when(item.rarity) {
                    Rarity.UNCOMMON -> "$baseMessage Uncommon item!"
                    Rarity.RARE -> "$baseMessage ${"Rare item!".bold()}"
                    else -> baseMessage
                }
            }
            textChannel?.queueSimpleEmbedMessage("Congratulations! New item!", item.rarity.color, message, item.imageLink)
            items += item
        }
    }

    private operator fun ArrayList<Item>.plusAssign(item: Item) {
        if (contains(item)) {
            val actualItem = items[items.indexOf(item)]
            actualItem.count += item.count
        } else {
            add(item)
        }
        ItemFunctions.saveItems(this@Player)
    }

    private operator fun ArrayList<Item>.minusAssign(item: Item) {
        remove(item)
        ItemFunctions.saveItems(this@Player)
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

/**
 * Represents the skills of a player
 *
 * @author Arham 4
 */
class Skills(val player: Player) {
    var totalLevel: Int = 0
        internal set
    var totalExp: Int = 0
        internal set
    var levels: Array<Int> = arrayOf()
        internal set
    var experiences: Array<Int> = arrayOf()
        internal set

    fun addExperience(skillId: Int, exp: Int) {
        experiences[skillId] += exp
        calculateLevel(skillId, exp)
        totalExp += exp
        saveStats()
    }

    private fun getLevelForExperience(experience: Int): Int {
        check(experience in 0.0..2.0E8)
        return (1..98).firstOrNull { experience < SkillsData.experienceForLevel[it + 1] } ?: 99
    }

    private fun calculateLevel(skillId: Int, exp: Int) {
        val tempLevel = levels[skillId]
        levels[skillId] = getLevelForExperience(experiences[skillId])
        if (0.rangeTo(2).contains(skillId) || skillId == 4 || skillId == 6) {
            addExperience(3, exp / 3)
        }
        if (levels[skillId] != tempLevel) {
            if (levels[skillId] != 1) {
                player.textChannel?.queueSimpleEmbedMessage("Congratulations! Level up!", Color(0xfdcf70),
                        "Congratulations ${player.asDiscordUser.asMention}! You are now " +
                                "level ${levels[skillId]} in ${SkillsData.skills.skillNameFor[skillId]}.",
                        SkillsData.imageIconFor(skillId))
            }
        }
    }

    private fun saveStats() {
        player.sql { dsl, table ->
            dsl.update(table).set(table.LEVELS, levels)
                    .set(table.EXPERIENCES, experiences)
                    .set(table.TOTAL_EXP, totalExp)
                    .set(table.TOTAL_LEVEL, totalLevel)
                    .where(table.DISCORD_ID.eq(player.asDiscordUser.id))
                    .execute()
        }
    }
}