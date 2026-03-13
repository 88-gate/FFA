package me.kaitp1016.ffa.items.impl.admin

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.packetgui.impl.kitselector.KitSelectGui
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.ItemStack

object FFAKitSelector: CustomItem() {
    override val id = "FFA_KIT_SELECTOR"
    override val name = "FFAキット選択"
    override val material = Material.BOOK
    override val rarity = Rarity.ADMIN
    override val category = ItemCategory.ADMIN

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:knowledge_book"))
        }.bukkitStack
    }

    @ItemEventHandler
    fun onUsed(event: ItemEvents.UseEvent) {
        val player = event.player
        player.getAttribute(Attribute.MAX_HEALTH)?.baseValue = 40.0
        KitSelectGui(player.asCraftPlayer().handle,listOf(),true).open()
    }
}