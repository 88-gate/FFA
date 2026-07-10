package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.EquipmentSlot

class EarlyAdvantagePerk: Perk() {
    override val icon = ItemStack(Items.REDSTONE)
    override val name = "Early Advantage"
    override val cost = 10000
    override val duplicateLimit = 1
    override val description = listOf(
        "初期装備にダイヤモンドのヘルメットが含まれるようになる。",
    )

    override fun onRespawn(player: Player, event: PlayerRespawnEvent) {
        player.inventory.setItem(EquipmentSlot.HEAD, ItemStack(Items.DIAMOND_HELMET).bukkitStack)
    }
}