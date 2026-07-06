package me.kaitp1016.ffa.park.gui

import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.ChestPacketGui
import me.kaitp1016.ffa.park.ParkManager
import me.kaitp1016.ffa.park.Parks
import me.kaitp1016.ffa.utils.DatapackAPI.getMoney
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.item.component.TooltipDisplay

class ParkMainGui: ChestPacketGui {
    override val displayName = net.kyori.adventure.text.Component.text("パーク")
    override val name = "Park Main"

    val park: ParkManager.PlayerPark

    constructor(player: ServerPlayer, parent: AbstractPacketGui?) : super(player, 27) {
        this.parent = parent
        this.park = ParkManager.getPark(player.uuid)
    }

    override fun onOpen() {
        items.fill(ItemStack(Items.GRAY_STAINED_GLASS_PANE).apply {
            set(DataComponents.ITEM_NAME, Component.empty())
            set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay(true,linkedSetOf()))
        })

        for (slot in 9..17) {
            setItem(slot, getParkItem(slot))
        }

        setOpenParentItem(22)

        super.onOpen()
    }

    override fun onClick(packet: ServerboundContainerClickPacket) {
        val slot = packet.slotNum.toInt()
        val index = getParkIndex(slot)
        if (index !in 0..8) {
            update(false)
            return
        }

        if (!isUnlocked(index)) {
            val cost = ParkManager.getUnlockCost(park.unlockedSlots + 1)
            if (player.getMoney() < cost) {
                update(false)
                player.sendSystemMessage(Component.literal("ポイントが足りません!"))
                player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.SPLASH_POTION_BREAK), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))
                return
            }

            BuySlotGui(player,this).open()
            player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.EXPERIENCE_ORB_PICKUP), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))

            return
        }

        ParkSelectGui(player,index,this).open()

        player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.EXPERIENCE_ORB_PICKUP), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))
    }

    private fun getParkItem(slot: Int): ItemStack {
        val index = getParkIndex(slot)
        if (!isUnlocked(index)) {
            val cost = ParkManager.getUnlockCost(park.unlockedSlots + 1)
            return ItemStack(Items.RED_STAINED_GLASS_PANE).apply {
                set(DataComponents.ITEM_NAME, Component.literal("Park #${index + 1} (未開放)").withColor(0xFF55FF))
                set(DataComponents.LORE, ItemLore(listOf(Component.literal("必要ポイント: $cost").withStyle(Style.EMPTY.withItalic(false).withColor(0xFFFF55)))))
            }
        }

        val park = park.selectedParks.getOrNull(index) ?: Parks.EMPTY

        return park.icon.copy().apply {
            set(DataComponents.ITEM_NAME, Component.literal("Park #${index + 1}").withColor(0xFF55FF).append(Component.literal(" (${park.name})").withColor(0xFFFF55)))
            set(DataComponents.LORE, ItemLore(park.description.map { Component.literal(it).withStyle(Style.EMPTY.withItalic(false).withColor(0xFFFFFF)) }))
        }
    }

    private fun getParkIndex(slot: Int): Int {
        return slot - 9
    }

    private fun isUnlocked(index: Int): Boolean {
        return park.unlockedSlots > index
    }
}