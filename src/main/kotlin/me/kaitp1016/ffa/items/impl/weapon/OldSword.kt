package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Unit
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemUseAnimation
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.ItemAttributeModifiers
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe

object OldSword: CustomItem() {
    override val id = "OLD_SWORD"
    override val name = "Old Sword"
    override val material = Material.NETHERITE_SWORD
    override val rarity = Rarity.LEGENDARY
    override val category = ItemCategory.WEAPON
    override val description = "この剣でダメージを与えた時、\n8ダメージ以上の攻撃は8ダメージになる。\n右クリックでガードができる。\nガード中は全ての受けるダメージを30%軽減する。"

    const val MAX_DAMAGE = 8.0

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers(
                listOf(
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 5.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, 96.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                )
            ))

            this.set(DataComponents.CONSUMABLE, Consumable(Float.MAX_VALUE, ItemUseAnimation.BLOCK, Holder.direct(SoundEvents.EMPTY),false,listOf()))
            this.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:diamond_sword"))
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        }.bukkitStack
    }

    override fun getRecipes(): List<Recipe> {
        return listOf(ShapedRecipe(NamespacedKey(plugin,"old_sword"), createItem(1)).apply {
            this.shape("IHI","QSQ","IDI")
            this.setIngredient('I', Material.IRON_BLOCK)
            this.setIngredient('H', Material.PLAYER_HEAD)
            this.setIngredient('Q', Material.QUARTZ)
            this.setIngredient('S', Material.DIAMOND_SWORD)
            this.setIngredient('D', Material.DIAMOND)
        })
    }

    @ItemEventHandler
    fun onDamageWhileUsing(event:ItemEvents.DamageWhileUsingEvent) {
        event.bukkitEvent.damage = event.bukkitEvent.damage * 0.7
    }

    @ItemEventHandler
    fun onDamage(event: ItemEvents.DamageEntityEvent) {
        val damage = event.bukkitEvent.damage
        if (damage >= MAX_DAMAGE) {
            event.bukkitEvent.damage = MAX_DAMAGE
        }
    }
}