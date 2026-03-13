package me.kaitp1016.ffa.items.impl.admin

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.Utils.equalsOneOf
import net.minecraft.core.component.DataComponents
import net.minecraft.util.Unit
import org.bukkit.Material
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.ItemStack

object GrapplingHook: CustomItem() {
    override val id = "GRAPPLING_HOOK"
    override val name = "グラップリングフック"
    override val material = Material.FISHING_ROD
    override val rarity = Rarity.ADMIN
    override val category = ItemCategory.ADMIN

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        }.bukkitStack
    }

    @ItemEventHandler
    fun onFish(event: ItemEvents.FishEvent) {
        if (!event.bukkitEvent.state.equalsOneOf(PlayerFishEvent.State.REEL_IN, PlayerFishEvent.State.IN_GROUND)) return
        val player = event.player

        val velocity = event.bukkitEvent.hook.location.clone().subtract(player.location).multiply(0.5).apply {
            this.y = 0.2 + this.y / 3
            this.add(player.velocity)
        }

        player.velocity = velocity.toVector()
    }
}