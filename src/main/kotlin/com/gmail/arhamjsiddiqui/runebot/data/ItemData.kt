package com.gmail.arhamjsiddiqui.runebot.data

import java.util.*

object ItemData {
    data class ItemsDto(val commonItems: Array<Int>, val uncommonItems: Array<Int>, val rareItems: Array<Int>, private val statRequirements: Map<String, Array<Any>>) {
        /**
         * Need to find a better way than to do Array<Any> @ statRequirements
         */

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ItemsDto

            if (!Arrays.equals(commonItems, other.commonItems)) return false
            if (!Arrays.equals(uncommonItems, other.uncommonItems)) return false
            if (!Arrays.equals(rareItems, other.rareItems)) return false
            if (statRequirements != other.statRequirements) return false

            return true
        }

        override fun hashCode(): Int {
            var result = Arrays.hashCode(commonItems)
            result = 31 * result + Arrays.hashCode(uncommonItems)
            result = 31 * result + Arrays.hashCode(rareItems)
            result = 31 * result + statRequirements.hashCode()
            return result
        }

        fun getRequirementsForItem(name: String): List<Array<Any>> {
            return statRequirements.filter { entry -> name.toLowerCase().startsWith(entry.key.toLowerCase()) }.map { it -> it.value }
        }
    }
    val items: ItemsDto = YAMLParse.parseDto("data/items_data.yaml", ItemsDto::class)
}