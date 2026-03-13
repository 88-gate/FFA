package me.kaitp1016.ffa.items.impl.consumeable

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.entity.TNTPrimed
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

object ThrowableTNT: CustomItem() {
    override val id = "THROWABLE_TNT"
    override val name = "投げれるTNT"
    override val material = Material.TNT
    override val rarity = Rarity.UNCOMMON
    override val category = ItemCategory.CONSUMEABLE

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin, "throwable_tnt"), this.createItem(1)).apply {
                this.shape(" S ", "SGS", " S ")
                this.setIngredient('G', Material.GUNPOWDER)
                this.setIngredient('S', Material.SAND)
            }
        )
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        event.isCancelled = true

        val player = event.player
        if (!this.consumeOrMessage(player,amount = 1)) return

        (player.world.spawnEntity(player.location.add(0.0,player.eyeHeight,0.0), EntityType.TNT) as TNTPrimed).apply {
            this.velocity = player.location.direction.clone().apply {
                this.add(player.velocity)
                this.y = this.y * 0.5 + 0.3
            }

            this.source = player
            this.fuseTicks = 35
        }
    }
}