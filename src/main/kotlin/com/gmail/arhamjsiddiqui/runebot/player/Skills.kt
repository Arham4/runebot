package com.gmail.arhamjsiddiqui.runebot.player

import com.gmail.arhamjsiddiqui.runebot.RuneBot
import com.gmail.arhamjsiddiqui.runebot.YAMLParse
import com.gmail.arhamjsiddiqui.runebot.asProperSubjectType
import com.gmail.arhamjsiddiqui.runebot.player.SkillsData.experienceForLevel
import com.gmail.arhamjsiddiqui.runebot.sendMessage

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
        calculateLevel(skillId)
        totalExp += exp
        saveStats()
    }

    private fun getLevelForExperience(experience: Int): Int {
        check(experience in 0.0..2.0E8)
        return (1..98).firstOrNull { experience < experienceForLevel[it + 1] } ?: 99
    }

    private fun calculateLevel(skillId: Int) {
        val tempLevel = levels[skillId]
        levels[skillId] = getLevelForExperience(experiences[skillId])
        if (levels[skillId] != tempLevel) {
            val levelGain = Math.abs(levels[skillId] - tempLevel)
            RuneBot.BOT.sendMessage("Congratulations ${player.asDiscordUser.asMention}! You've leveled up $levelGain " +
                    "${"level".asProperSubjectType(levelGain)} in ${SkillsData.skills.skillNameFor[skillId]?.capitalize()}! You are now level ${levels[skillId]}.")
        }
    }

    private fun saveStats() {
        player.sql { dsl, table ->
            dsl.update(table).set(table.LEVELS, levels)
                    .set(table.EXPERIENCES, experiences)
                    .set(table.TOTAL_EXP, totalExp)
                    .set(table.TOTAL_LEVEL, totalLevel)
                    .execute()
        }
    }
}

object SkillsData {
    data class SkillsDto(val skillNameFor: Map<Int, String>, val skillIdFor: Map<String, Int>, val skillNameForNickname: Map<String, String>)
    val skills: SkillsDto = YAMLParse.parseDto("data/skills_data.yaml", SkillsDto::class)

    val experienceForLevel: Array<Int> = let {
        val experienceForLevel = Array(100, {0})
        var points = 0
        var output = 0
        for (level in 1..99) {
            experienceForLevel[level] = output
            points += Math.floor(level + 300 * Math.pow(2.0, level / 7.0)).toInt()
            output = Math.floor((points / 4).toDouble()).toInt()
        }
        experienceForLevel
    }
}