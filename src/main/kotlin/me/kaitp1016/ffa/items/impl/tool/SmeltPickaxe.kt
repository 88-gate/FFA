package me.kaitp1016.ffa.items.impl.tool

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftBlockState
import me.kaitp1016.ffa.utils.RecipeUtils
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

object SmeltPickaxe: CustomItem() {
    override val id = "SMELT_PICKAXE"
    override val name = "焼けるツルハシ"
    override val material = Material.IRON_PICKAXE
    override val rarity = Rarity.UNCOMMON
    override val category = ItemCategory.TOOL

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin, "smelt_pickaxe"), this.createItem(1)).apply {
                this.shape("III","CSC"," S ")
                this.setIngredient('I', Material.RAW_IRON)
                this.setIngredient('C', Material.COAL)
                this.setIngredient('S', Material.STICK)
            }
        )
    }

    @ItemEventHandler
    fun onBlockDropItem(event: ItemEvents.BlockDropItemEven) {
        if (event.bukkitEvent.blockState.asCraftBlockState().handle.hasBlockEntity()) return

        event.bukkitEvent.items.forEach {item ->
            val recipe = RecipeUtils.furnaceRecipes.find { it.inputChoice.test(item.itemStack) }
            if (recipe == null) return@forEach

            item.itemStack = recipe.result.apply {
                this.amount = item.itemStack.amount
            }
        }
    }
}