package me.kaitp1016.ffa.items.events

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack

object ArmorEvents {
    class DamageEvent: CancellableItemEvent {
        val bukkitEvent: EntityDamageEvent

        constructor(item: ItemStack,player: Player,bukkitEvent: EntityDamageEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }
}