package me.kaitp1016.ffa.items.impl.admin

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import org.bukkit.Material

object FlyFeather: CustomItem() {
    override val id = "FLY_FEATHER"
    override val name = "軽い羽"
    override val material = Material.FEATHER
    override val rarity = Rarity.ADMIN
    override val category = ItemCategory.ADMIN

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        val player = event.player
        if (!consumeOrMessage(player)) return

        val velocity = player.location.direction.clone().multiply(2).apply {
            this.add(player.velocity)
            this.y = this.y * 0.5 + 0.5
        }

        player.velocity = velocity
        player.fallDistance = -500f
    }
}