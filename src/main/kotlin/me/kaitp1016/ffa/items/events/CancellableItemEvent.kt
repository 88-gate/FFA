package me.kaitp1016.ffa.items.events

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class CancellableItemEvent: ItemEvent {
    constructor(item: ItemStack,player: Player):super(item,player)

    var isCancelled = false
}