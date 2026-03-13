package me.kaitp1016.ffa.items.impl.tool

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import net.minecraft.core.component.DataComponents
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

object DiamondShear: CustomItem() {
    override val id = "DIAMOND_SHEAR"
    override val name = "ダイヤモンドのハサミ"
    override val material = Material.SHEARS
    override val rarity = Rarity.RARE
    override val category = ItemCategory.TOOL

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.MAX_DAMAGE,640)
        }.bukkitStack
    }

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin, "diamond_shear"), this.createItem(1)).apply {
                this.shape(" D","D ")
                this.setIngredient('D', Material.DIAMOND)
            }
        )
    }
}