package me.kaitp1016.ffa.items.events

import me.kaitp1016.ffa.items.ItemManager.getBattleRoyalItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class ItemEvent {
    val item: ItemStack
    val player: Player

    constructor(item: ItemStack,player: Player) {
        this.item = item
        this.player = player
    }

    fun post() {
        val battleRoyalItem = item.getBattleRoyalItem()
        if (battleRoyalItem == null) return

        ItemEventManager.post(this, battleRoyalItem.internalId)
    }

    fun post(itemId: Int) {
        ItemEventManager.post(this,itemId)
    }
}