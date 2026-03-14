package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import net.minecraft.core.component.DataComponents
import net.minecraft.util.Unit
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object HealingSword: CustomItem() {
    override val id = "HEALING_SWORD"
    override val name = "ヒーリングソード"
    override val material = Material.IRON_SWORD
    override val rarity = Rarity.RARE
    override val category = ItemCategory.WEAPON
    override val isEnchantable = false

    override val description = "この剣で敵を殴ると、\n殴られたプレイヤーのハートが3つ分回復する。\nクールダウンは5秒。"

    const val COOLDOWN_TICKS = 5 * 20 // 5秒

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers(
                listOf(
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 5.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                )
            ))
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        }.bukkitStack
    }

    @ItemEventHandler
    fun onAttack(event: ItemEvents.DamageEntityEvent) {
        val target = event.bukkitEvent.entity as? Player ?: return
        val attacker = event.player

        if (attacker.hasCooldown(material)) return

        attacker.setCooldown(material, COOLDOWN_TICKS)
        val maxHealth = target.getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0
        target.health = (target.health + 6.0).coerceAtMost(maxHealth)
    }
}
