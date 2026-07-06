package me.kaitp1016.ffa.park.gui

import me.kaitp1016.ffa.mc
import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.ChestPacketGui
import me.kaitp1016.ffa.park.Park
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

class ParkSelectGui: ChestPacketGui {
    override val displayName = net.kyori.adventure.text.Component.text("パーク")
    override val name = "Park Main"

    var index: Int
    val park: ParkManager.PlayerPark
    val parks:List<Park>

    constructor(player: ServerPlayer, index: Int, parent: AbstractPacketGui?) : super(player, 36) {
        this.parent = parent
        this.index = index
        this.park = ParkManager.getPark(player.uuid)
        this.parks = Parks.entries()
    }

    override fun onOpen() {
        items.fill(ItemStack(Items.GRAY_STAINED_GLASS_PANE).apply {
            set(DataComponents.ITEM_NAME, Component.empty())
            set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay(true,linkedSetOf()))
        })

        parks.forEachIndexed { index, park ->
            setItem(index, getParkItem(park))
        }

        setOpenParentItem(32)

        super.onOpen()
    }

    override fun onClick(packet: ServerboundContainerClickPacket) {
        val slot = packet.slotNum.toInt()
        val park = parks.getOrNull(slot)
        if (park == null) {
            update(false)
            return
        }

        if (!isUnlocked(park)) {
            val cost = park.cost
            if (player.getMoney() < cost) {
                player.sendSystemMessage(Component.literal("ポイントが足りません!"))
                player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.SPLASH_POTION_BREAK), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))
                update(false)
                return
            }

            ConfirmBuyParkGui(player,park,this).open()
            player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.EXPERIENCE_ORB_PICKUP), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))

            return
        }

        mc.execute {
            this.park.selectedParks[index] = park
            player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.EXPERIENCE_ORB_PICKUP), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))
            openParent()
        }
    }

    private fun getParkItem(park: Park): ItemStack {
        if (!isUnlocked(park)) {
            return ItemStack(Items.RED_STAINED_GLASS_PANE).apply {
                set(DataComponents.ITEM_NAME, Component.literal(park.name).withColor(0xFFFF55))
                set(DataComponents.LORE, ItemLore(park.description.map { Component.literal(it).withStyle(Style.EMPTY.withItalic(false).withColor(0xFFFFFF)) }.toMutableList().also {
                    it.add(Component.empty())
                    it.add(Component.literal("必要ポイント: ${park.cost}").withStyle(Style.EMPTY.withItalic(false).withColor(0xFFFFFF)))
                }.toList()))
            }
        }


        return park.icon.copy().apply {
            set(DataComponents.ITEM_NAME, Component.literal(park.name).withColor(0x55FF55))
            set(DataComponents.LORE, ItemLore(park.description.map { Component.literal(it).withStyle(Style.EMPTY.withItalic(false).withColor(0xFFFFFF)) }))
        }
    }

    private fun isUnlocked(park: Park): Boolean {
        return this.park.unlockedParks.contains(park)
    }
}