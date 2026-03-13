package me.kaitp1016.ffa.items.impl.tool

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftBlockState
import me.kaitp1016.ffa.utils.RecipeUtils
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

object FlintShovel: CustomItem() {
    override val id = "FLINT_SHOVEL"
    override val name = "火打石のシャベル"
    override val material = Material.IRON_SHOVEL
    override val rarity = Rarity.COMMON
    override val category = ItemCategory.TOOL
    override val description = "このシャベルで壊して出たアイテムは\n焼かれた状態で手に入る。"

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).apply {
            this.addUnsafeEnchantment(Enchantment.FORTUNE,3)
            this.addUnsafeEnchantment(Enchantment.EFFICIENCY,5)
            this.addUnsafeEnchantment(Enchantment.UNBREAKING,10)
        }
    }

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin,"flint_shovel"),this.createItem(1)).apply {
                this.shape("F","S","S")
                this.setIngredient('F', Material.FLINT)
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