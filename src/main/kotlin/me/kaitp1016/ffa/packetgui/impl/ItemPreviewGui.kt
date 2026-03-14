package me.kaitp1016.ffa.packetgui.impl

import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.ChestPacketGui
import net.kyori.adventure.text.Component
import net.minecraft.core.component.DataComponents
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.TooltipDisplay

class ItemPreviewGui: ChestPacketGui {
    override val name: String
    override val displayName: Component

    constructor(player: ServerPlayer, item: ItemStack, parent: AbstractPacketGui? = null):super(player,27) {
        this.items.fill(ItemStack(Items.GRAY_STAINED_GLASS_PANE).apply {
            this.set(DataComponents.ITEM_NAME, net.minecraft.network.chat.Component.literal(""))
            this.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay(true,linkedSetOf()))
        })
        this.setItem(13,item)
        setOpenParentItem(22)

        this.name = item.hoverName.string
        this.displayName = Component.text("アイテム: ").append(item.bukkitStack.displayName())
        this.parent = parent
    }

    override fun onClick(packet: ServerboundContainerClickPacket) {
        this.update(false)
    }
}