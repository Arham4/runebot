package com.gmail.arhamjsiddiqui.runebot.entity

import com.gmail.arhamjsiddiqui.runebot.data.YAMLParse
import com.mikebull94.rsapi.RuneScapeAPI
import java.awt.Color
import java.util.*

/**
 * Represents items that can be acquired.
 *
 * @author Arham 4
 */
data class Item(val id: Int, var count: Int = 1, val rarity: Rarity = Rarity.COMMON) {
    val definition by lazy { GRAND_EXCHANGE_API.itemPriceInformation(id).get().item }
    val name by lazy { definition.name }
    val imageLink = "https://www.runelocus.com/items/img/$id.png"

    override fun equals(other: Any?): Boolean {
        if (other is Item) return other.id == id
        return false
    }

    override fun hashCode(): Int {
        return id
    }
}

enum class Rarity(val color: Color) {
    COMMON(Color.YELLOW), UNCOMMON(Color.ORANGE), RARE(Color.RED)
}

data class ItemsDto(val commonItems: Array<Int>, val uncommonItems: Array<Int>, val rareItems: Array<Int>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemsDto

        if (!Arrays.equals(commonItems, other.commonItems)) return false
        if (!Arrays.equals(uncommonItems, other.uncommonItems)) return false
        if (!Arrays.equals(rareItems, other.rareItems)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(commonItems)
        result = 31 * result + Arrays.hashCode(uncommonItems)
        result = 31 * result + Arrays.hashCode(rareItems)
        return result
    }
}

val items: ItemsDto = YAMLParse.parseDto("data/items_data.yaml", ItemsDto::class)
val GRAND_EXCHANGE_API = RuneScapeAPI.createHttp().grandExchange()