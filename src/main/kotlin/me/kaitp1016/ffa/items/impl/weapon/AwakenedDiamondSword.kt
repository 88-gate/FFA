package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object AwakenedDiamondSword: CustomItem() {
    override val id = "AWAKENED_DIAMOND_SWORD"
    override val name = "Awakened Diamond Sword"
    override val material = Material.DIAMOND_SWORD
    override val rarity = Rarity.EPIC
    override val category = ItemCategory.WEAPON

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(
                DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers(
                    listOf(
                        ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 11.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                        ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                    )
                )
            )
        }.bukkitStack
    }

    /*
    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin,"awakened_diamond_sword"), this.createItem(1)).apply {
                this.shape("D","S","H")
                this.setIngredient('D', Material.DIAMOND_BLOCK)
                this.setIngredient('S', RecipeUtils.BattleRoyalItemChoice(RefinedDiamondSword))
                this.setIngredient('H',Material.PLAYER_HEAD)
            }
        )
    }

     */
}