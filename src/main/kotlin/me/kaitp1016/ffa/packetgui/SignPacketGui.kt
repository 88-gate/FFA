package me.kaitp1016.ffa.packetgui

import me.kaitp1016.ffa.events.impl.PacketReciveEvent
import me.kaitp1016.ffa.events.impl.PacketSendEvent
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import net.kyori.adventure.text.Component
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.entity.SignText

abstract class SignPacketGui: AbstractPacketGui {
    private val lines: Array<net.minecraft.network.chat.Component>

    val syncId: Int
    var signPos: BlockPos? = null

    constructor(player: ServerPlayer,lines: List<Component>):super(player) {
        this.lines = lines.map { it.toMCComponent() }.toTypedArray()
        this.syncId = player.nextContainerCounter()
    }

    abstract fun onComplete(lines: List<String>)

    override fun open() {
        signPos = BlockPos(player.x.toInt(), player.y.toInt() - 4, player.z.toInt())

        player.doCloseContainer()

        player.containerMenu = PacketGuiContainer(syncId, this)

        val sign = SignBlockEntity(signPos!!, Blocks.OAK_SIGN.defaultBlockState()).apply {
            this.setText(SignText(lines, lines, DyeColor.BLACK, false), true)
            this.setLevel(player.level())
        }

        player.connection.send(ClientboundBlockUpdatePacket(signPos!!, sign.blockState))
        player.connection.send(sign.updatePacket)
        player.connection.send(ClientboundOpenSignEditorPacket(signPos!!, true))
        this.isOpened = true
    }

    override fun onPacketSend(event: PacketSendEvent) {

    }

    override fun onPacketRecive(event: PacketReciveEvent) {
        val packet = event.packet
        if (packet is ServerboundSignUpdatePacket) {
            onComplete(packet.lines.toList())
            onClose()

            closeInternal()
        }
    }

    override fun close() {
        closeInternal()
    }

    fun closeInternal() {
        player.doCloseContainer()
        isOpened = false

        if (signPos != null) {
            val world = player.level()
            val block = world.getBlockState(signPos!!)
            player.connection.send(ClientboundBlockUpdatePacket(signPos!!,block))
            if (block.hasBlockEntity()) {
                player.connection.send(world.getBlockEntity(signPos!!)?.updatePacket ?: return)
            }
        }
    }

    override fun onOpen() {

    }

    override fun onClose() {

    }
}