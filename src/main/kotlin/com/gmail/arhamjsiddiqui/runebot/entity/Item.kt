package com.gmail.arhamjsiddiqui.runebot.entity

import com.gmail.arhamjsiddiqui.runebot.data.ItemData.items
import com.gmail.arhamjsiddiqui.runebot.randomItem
import com.mikebull94.rsapi.RuneScapeAPI
import java.awt.Color
import java.util.concurrent.ThreadLocalRandom

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

object ItemFunctions {
    fun generateRandomItem(): Item {
        val randomNumber = ThreadLocalRandom.current().nextInt(1, 101)
        return when (randomNumber) {
            in 1..50 -> Item(items.commonItems.randomItem(), rarity = Rarity.COMMON)
            in 51..80 -> Item(items.uncommonItems.randomItem(), rarity = Rarity.UNCOMMON)
            else -> Item(items.rareItems.randomItem(), rarity = Rarity.RARE)
        }
    }

    fun saveItems(player: Player) {
        Player.sql { dsl, table ->
            dsl.update(table).set(table.ITEM_IDS, player.items.map { it.id }.toTypedArray())
                    .set(table.ITEM_COUNTS, player.items.map{ it.count }.toTypedArray())
                    .where(table.DISCORD_ID.eq(player.asDiscordUser.id))
                    .execute()
        }
    }
}
val GRAND_EXCHANGE_API = RuneScapeAPI.createHttp().grandExchange()