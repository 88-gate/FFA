package me.kaitp1016.ffa.items.impl.admin

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.items.ItemCategory
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object AdminSword: CustomItem() {
    override val id = "ADMIN_SWORD"
    override val name = "Admin Sword"
    override val rarity = Rarity.ADMIN
    override val material = Material.DIAMOND_SWORD
    override val category = ItemCategory.ADMIN

    override fun getDisplayName(item: ItemStack?): Component {
        return Component.text("admin sword (V"+Math.random()+")")
    }

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).apply {
            this.editMeta {
                it.setMaxStackSize(63)
            }
        }
    }

    @ItemEventHandler
    fun onAttack(event: ItemEvents.DamageEntityEvent) {
        event.bukkitEvent.damage = 10000.0
        event.player.sendMessage("attack! ${event.bukkitEvent.entity.name}")
    }

    @ItemEventHandler
    fun onSwing(event: ItemEvents.SwingEvent) {
        event.player.sendMessage("swing!")
    }
    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        event.player.sendMessage("use!")
        event.isCancelled = true
    }
}