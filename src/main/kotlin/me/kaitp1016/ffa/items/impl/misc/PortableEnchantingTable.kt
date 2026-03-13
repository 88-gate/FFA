package me.kaitp1016.ffa.items.impl.misc

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.plugin
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.MenuType
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

object PortableEnchantingTable: CustomItem() {
    override val id: String = "PORTABLE_ENCHANTING_TABLE"
    override val name = "持ち運び式エンチャントテーブル"
    override val rarity = Rarity.RARE
    override val material = Material.ENCHANTING_TABLE
    override val category = ItemCategory.MISC

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin,"portable_enchanting_table"), this.createItem(1)).apply {
                this.shape("E","S",)
                this.setIngredient('E', Material.ENCHANTING_TABLE)
                this.setIngredient('S',Material.STICK)
            }
        )
    }

    @ItemEventHandler
    fun onUsed(event: ItemEvents.UseEvent) {
        event.isCancelled = true

        val player = event.player
        player.openInventory(MenuType.ENCHANTMENT.create(player))
    }
}