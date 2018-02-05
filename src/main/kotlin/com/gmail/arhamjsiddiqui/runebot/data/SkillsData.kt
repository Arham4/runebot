package com.gmail.arhamjsiddiqui.runebot.data

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

    fun imageIconFor(skillId: Int): String {
        return "https://www.tip.it/runescape/images/misc/skill_${skills.skillNameFor[skillId]?.toLowerCase()}_logo.png"
    }
}