package main.kotlin.com.gmail.arhamjsiddiqui.runebot.player

/**
 * Represents the skills of a player
 *
 * @author Arham 4
 */
class Skills {
    var totalLevel: Int = 0
        internal set
    var totalExp: Int = 0
        internal set
    var levels: Array<Int> = arrayOf()
        internal set
    var experiences: Array<Int> = arrayOf()
        internal set

    operator fun set(skillId: Int, exp: Int) {
        experiences[skillId] += exp
        calculateLevel(skillId)
        totalExp += exp
    }

    private fun getLevelForExperience(experience: Int): Int {
        check(experience in 0.0..2.0E8)
        return (1..98).firstOrNull { experience < experienceForLevel[it + 1] } ?: 99
    }

    private fun calculateLevel(skillId: Int) {
        val tempLevel = levels[skillId]
        levels[skillId] = getLevelForExperience(experiences[skillId])
        if (levels[skillId] != tempLevel) Math.abs(levels[skillId] - tempLevel)
    }
}

private val experienceForLevel by lazy {
    val experienceForLevel: IntArray = intArrayOf()
    var points = 0
    var output = 0
    for (level in 1..99) {
        experienceForLevel[level] = output
        points += Math.floor(level + 300 * Math.pow(2.0, level / 7.0)).toInt()
        output = Math.floor((points / 4).toDouble()).toInt()
    }
    experienceForLevel
}