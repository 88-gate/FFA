package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftEntity
import net.minecraft.world.entity.animal.Animal
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

object FlintSword: CustomItem() {
    override val id = "FLINT_SWORD"
    override val name = "火打石の剣"
    override val material = Material.IRON_SWORD
    override val rarity = Rarity.COMMON
    override val category = ItemCategory.WEAPON

    override val description = "動物以外にダメージを与えれない。\n動物に対してのダメージが2倍になる。"

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).apply {
            this.addUnsafeEnchantment(Enchantment.FIRE_ASPECT,2)
            this.addUnsafeEnchantment(Enchantment.LOOTING,3)
        }
    }

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin,"flint_sword"),this.createItem(1)).apply {
                this.shape("F","F","S")
                this.setIngredient('F', Material.FLINT)
                this.setIngredient('S', Material.STICK)
            }
        )
    }

    @ItemEventHandler
    fun onDamage(event: ItemEvents.DamageEntityEvent) {
        if (event.bukkitEvent.entity.asCraftEntity().handle is Animal) {
            event.bukkitEvent.damage = event.bukkitEvent.damage * 2
        }
        else {
            event.isCancelled = true
        }
    }
}