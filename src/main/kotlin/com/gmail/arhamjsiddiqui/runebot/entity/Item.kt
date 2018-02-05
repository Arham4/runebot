package com.gmail.arhamjsiddiqui.runebot.entity

import com.mikebull94.rsapi.RuneScapeAPI

/**
 * Represents items that can be aquired.
 *
 * @author Arham 4
 */
data class Item(val id: Int, val count: Int = 1) {
    val definition = GRAND_EXCHANGE_API.itemPriceInformation(id).get().item
    val imageLink = "https://www.runelocus.com/items/img/$id.png"
}

val GRAND_EXCHANGE_API = RuneScapeAPI.createHttp().grandExchange()

fun ArrayList<Item>.add(id: Int, count: Int = 1, action: (item: Item) -> Unit) {
    val item = Item(id, count)
    add(item)
    action.invoke(item)
}