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
import org.bukkit.entity.Fireball
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

object Fireball: CustomItem() {
    override val id = "FIREBALL"
    override val name = "ファイヤーボール"
    override val material = Material.FIRE_CHARGE
    override val rarity = Rarity.UNCOMMON
    override val category = ItemCategory.CONSUMEABLE
    override val description = "右クリックでアイテムを消費して\nファイヤーボールを召喚する。"

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin, "fireball"), this.createItem(4)).apply {
                this.shape(" F ","FIF"," F ")
                this.setIngredient('F', Material.FLINT)
                this.setIngredient('I', Material.IRON_INGOT)
            }
        )
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        val player = event.player
        if (!this.consumeOrMessage(player,amount = 1)) return

        (player.world.spawnEntity(player.location.add(0.0,player.eyeHeight,0.0), EntityType.FIREBALL) as Fireball).apply {
            this.direction = player.location.direction.multiply(0.1f)
            this.shooter = player
            this.yield = 3f
        }
    }
}