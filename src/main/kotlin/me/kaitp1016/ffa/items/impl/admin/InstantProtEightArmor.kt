package me.kaitp1016.ffa.items.impl.admin

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object InstantProtEightArmor: CustomItem() {
    override val id = "INSTANT_PROT_EIGHT_ARMOR"
    override val name = "prot 8 dia armor"
    override val material = Material.BOOK
    override val rarity = Rarity.ADMIN
    override val category = ItemCategory.ADMIN

    val armors = mapOf(
        EquipmentSlot.HEAD to ItemStack(Material.DIAMOND_HELMET).apply {
            this.addUnsafeEnchantment(Enchantment.PROTECTION,2)
            this.addUnsafeEnchantment(Enchantment.UNBREAKING,3)
            this.addUnsafeEnchantment(Enchantment.VANISHING_CURSE,1)
        },
        EquipmentSlot.CHEST to ItemStack(Material.DIAMOND_CHESTPLATE).apply {
            this.addUnsafeEnchantment(Enchantment.PROTECTION,2)
            this.addUnsafeEnchantment(Enchantment.UNBREAKING,3)
            this.addUnsafeEnchantment(Enchantment.VANISHING_CURSE,1)
        },
        EquipmentSlot.LEGS to ItemStack(Material.DIAMOND_LEGGINGS).apply {
            this.addUnsafeEnchantment(Enchantment.PROTECTION,2)
            this.addUnsafeEnchantment(Enchantment.UNBREAKING,3)
            this.addUnsafeEnchantment(Enchantment.VANISHING_CURSE,1)
        },
        EquipmentSlot.FEET to ItemStack(Material.DIAMOND_BOOTS).apply {
            this.addUnsafeEnchantment(Enchantment.PROTECTION,2)
            this.addUnsafeEnchantment(Enchantment.UNBREAKING,3)
            this.addUnsafeEnchantment(Enchantment.VANISHING_CURSE,1)
        },
    )

    @ItemEventHandler
    fun onUsed(event: ItemEvents.UseEvent) {
        val player = event.player
        if (!consumeOrMessage(player)) return

        armors.forEach {
            player.inventory.setItem(it.key,it.value)
        }
    }
}