package me.kaitp1016.ffa.packetgui.impl.kitselector

import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.ChestPacketGui
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.core.component.DataComponents
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemLore

class KitSelectGui: ChestPacketGui {
    override val displayName = Component.text("キット選択")
    override val name = "キット選択"

    var clearInventory:Boolean = false
    val kits: List<Kit>

    constructor(player: ServerPlayer, kits: List<Kit>,clearInventory:Boolean, parent: AbstractPacketGui? = null):super(player,54) {
        this.parent = parent
        this.kits = kits
        this.clearInventory = clearInventory

        update()
        setOpenParentItem(49)
    }

    override fun onClick(packet: ServerboundContainerClickPacket) {
        val slot = packet.slotNum.toInt()
        val kit = kits.getOrNull(slot) ?: run {
            player.sendSystemMessage(Component.text("キットが見つかりませんでした!").toMCComponent())
            update()
            return
        }

        if (packet.buttonNum.toInt() == 1) {
            KitPreviewGui(player,kit,this).open()
        }
        else {
            kit.give(player.bukkitEntity,clearInventory)
            close()
        }
    }

    override fun update(reopen: Boolean) {
        var index = -1
        kits.forEach { kit ->
            index++

            val lore = mutableListOf<String>()
            lore.add("説明：${kit.description}")
            lore.add("右クリックしてプレビュー")

            this.setItem(index, ItemStack(kit.symbol).apply {
                this.set(DataComponents.ITEM_NAME,Component.text(kit.name).toMCComponent())
                this.set(DataComponents.LORE, ItemLore(lore.map { Component.text(it).decoration(TextDecoration.ITALIC,false).color(NamedTextColor.WHITE).toMCComponent() }))
            })
        }

        super.update(reopen)
    }
}