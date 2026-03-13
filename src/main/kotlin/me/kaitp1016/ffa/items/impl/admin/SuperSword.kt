package me.kaitp1016.ffa.items.impl.admin

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import org.bukkit.Material
import org.bukkit.attribute.Attribute

object SuperSword: CustomItem() {
    override val id = "SUPER_SWORD"
    override val name = "スーパーソード"
    override val material = Material.WOODEN_SWORD
    override val rarity = Rarity.ADMIN
    override val category = ItemCategory.WEAPON
    override val description = "最強の剣"

    @ItemEventHandler
    fun onAttack(event: ItemEvents.DamageEntityEvent) {
        event.bukkitEvent.damage = 10000000.0
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        val player = event.player
        player.health = player.getAttribute(Attribute.MAX_HEALTH)!!.value
        player.foodLevel = 100
    }
}