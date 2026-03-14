package me.kaitp1016.ffa.game

import me.kaitp1016.ffa.packetgui.impl.ItemPreviewGui
import me.kaitp1016.ffa.utils.NMSUtils.asCraftEntity
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import org.bukkit.entity.ItemFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent

object ItemFramePreview: Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onInteract(event: PlayerInteractEntityEvent) {
        val entity = event.rightClicked
        if (entity !is ItemFrame || !entity.asCraftEntity().handle.tags.contains("ffa_previewable")) return

        val item = entity.item
        if (item.isEmpty) return

        val player = event.player.asCraftPlayer().handle
        ItemPreviewGui(player,item.asCraftItemStack().handle,null).open()
    }
}