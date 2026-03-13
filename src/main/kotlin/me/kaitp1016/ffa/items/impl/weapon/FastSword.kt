package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

object FastSword: CustomItem() {
    override val id = "FAST_SWORD"
    override val name = "殴るのが速い剣"
    override val material = Material.DIAMOND_SWORD
    override val rarity = Rarity.RARE
    override val category = ItemCategory.WEAPON
    override val description = "この剣でダメージを与えた時、\n7ダメージ以上の攻撃は7ダメージになる。"

    const val MAX_DAMAGE = 7.0

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers(
                listOf(
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 4.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, 96.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                )
            ))
            this.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:diamond_sword"))
        }.bukkitStack
    }

    override fun getRecipes(): List<Recipe>? {
        return listOf(ShapedRecipe(NamespacedKey(plugin,"fast_sword"), createItem(1)).apply {
            this.shape("GDG","GDG"," S ")
            this.setIngredient('G', Material.GOLD_INGOT)
            this.setIngredient('D', Material.DIAMOND)
            this.setIngredient('S', Material.STICK)
        })
    }

    @ItemEventHandler
    fun onDamage(event: ItemEvents.DamageEntityEvent) {
        val damage = event.bukkitEvent.damage
        if (damage >= MAX_DAMAGE) {
            event.bukkitEvent.damage = MAX_DAMAGE
        }
    }
}