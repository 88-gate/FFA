package me.kaitp1016.ffa.packetgui.impl.kitselector

import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.ChestPacketGui
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import net.kyori.adventure.text.Component
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.server.level.ServerPlayer
import org.bukkit.inventory.EquipmentSlot

class KitPreviewGui: ChestPacketGui {
    override val displayName = Component.text("キットプレビュー")
    override val name =" キットプレビュー"

    val kit: Kit

    constructor(player: ServerPlayer,kit: Kit,parent: AbstractPacketGui? = null):super(player,54) {
        this.kit = kit
        this.parent = parent
        setOpenParentItem(49)
        update()
    }

    override fun update(reopen: Boolean) {
        var i = -1
        kit.items.forEach {item ->
            i++
            this.setItem(i,item.asCraftItemStack().handle.copy())
        }

        kit.armors.forEach {
            val slot = slotMap[it.key]!!
            this.setItem(slot,it.value.asCraftItemStack().handle.copy())
        }

        super.update(reopen)
    }


    override fun onClick(packet: ServerboundContainerClickPacket) {
        update()
    }

    companion object {
        val slotMap = mapOf(
            EquipmentSlot.HEAD to 37,
            EquipmentSlot.CHEST to 38,
            EquipmentSlot.LEGS to 39,
            EquipmentSlot.FEET to 40,
            EquipmentSlot.OFF_HAND to 41,
        )
    }
}