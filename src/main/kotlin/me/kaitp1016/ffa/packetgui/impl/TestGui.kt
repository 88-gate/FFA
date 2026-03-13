package me.kaitp1016.ffa.packetgui.impl

import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.ChestPacketGui
import net.kyori.adventure.text.Component
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class TestGui: ChestPacketGui {
    override val name = "Test Gui"
    override val displayName get() = Component.text("test gui!! ${number}")
    var number = 1

    constructor(player: ServerPlayer,parent: AbstractPacketGui? = null):super(player,54) {
        this.setItem(4,ItemStack(Items.GOLD_BLOCK))
        this.setItem(5,ItemStack(Items.GOLD_BLOCK))
        this.setItem(6,ItemStack(Items.GOLD_BLOCK))
        this.setItem(7,ItemStack(Items.GOLD_BLOCK))
        this.setOpenParentItem(49)

        this.parent = parent
    }

    override fun onClick(packet: ServerboundContainerClickPacket) {
        number++
        val count = this.getItem(packet.slotNum.toInt()).count + 1
        this.setItem(packet.slotNum.toInt(), ItemStack(Items.REDSTONE).apply {
            this.count = count
        })

        this.update(true)
    }
}