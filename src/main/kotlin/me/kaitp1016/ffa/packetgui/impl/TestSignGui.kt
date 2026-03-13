package me.kaitp1016.ffa.packetgui.impl

import me.kaitp1016.ffa.events.impl.PacketSendEvent
import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.SignPacketGui
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import net.kyori.adventure.text.Component
import net.minecraft.server.level.ServerPlayer

class TestSignGui: SignPacketGui {
    override val name = "testgui"

    override fun onClose() {

    }

    override fun onOpen() {

    }

    override fun onPacketSend(event: PacketSendEvent) {

    }

    constructor(player: ServerPlayer,parent: AbstractPacketGui? = null):super(player,listOf(Component.text("a"),Component.text("b"),Component.text("c"),Component.text("d"))) {
        this.parent = parent
    }

    override fun onComplete(lines: List<String>) {
        player.sendSystemMessage(Component.text(lines.joinToString()).toMCComponent())
    }
}