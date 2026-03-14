package me.kaitp1016.ffa.packetgui

import me.kaitp1016.ffa.events.impl.PacketReciveEvent
import me.kaitp1016.ffa.events.impl.PacketSendEvent
import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftInventory
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

object PacketGuiManager: Listener {
    @EventHandler
    fun onPacketSend(event: PacketSendEvent) {
        val player = event.player.asCraftPlayer().handle

        val inv = player.containerMenu
        if (inv is PacketGuiContainer) {
            inv.packetGui.onPacketSend(event)
        }
    }

    @EventHandler
    fun onPacketRecive(event: PacketReciveEvent) {
        val player = event.player.asCraftPlayer().handle

        val inv = player.containerMenu
        if (inv is PacketGuiContainer) {
            inv.packetGui.onPacketRecive(event)
        }
    }

    @EventHandler
    fun onTick(event: TickEvent) {
        Bukkit.getOnlinePlayers().forEach { player ->
            val inv = player.asCraftPlayer().handle.containerMenu
            if (inv is PacketGuiContainer) {
                inv.packetGui.onTick()
            }
        }
    }
}