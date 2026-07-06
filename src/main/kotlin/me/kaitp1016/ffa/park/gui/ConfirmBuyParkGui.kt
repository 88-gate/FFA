package me.kaitp1016.ffa.park.gui

import me.kaitp1016.ffa.mc
import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.ChestPacketGui
import me.kaitp1016.ffa.park.Park
import me.kaitp1016.ffa.park.ParkManager
import me.kaitp1016.ffa.utils.DatapackAPI.addMoney
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

class ConfirmBuyParkGui: ChestPacketGui {
    override val displayName = net.kyori.adventure.text.Component.text("パークを買う")
    override val name = "Buy Park"

    val park: Park
    val playerPark: ParkManager.PlayerPark

    constructor(player: ServerPlayer, park: Park, parent: AbstractPacketGui?) : super(player, 27) {
        this.parent = parent
        this.park = park
        this.playerPark = ParkManager.getPark(player.uuid)

        items.fill(ItemStack(Items.GRAY_STAINED_GLASS_PANE).apply {
            set(DataComponents.ITEM_NAME, Component.empty())
            set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay(true, linkedSetOf()))
        })

        setItem(BUY_SLOT, ItemStack(Items.LIME_CONCRETE).apply {
            set(DataComponents.ITEM_NAME, Component.literal("買う"))
        })

        setItem(CANCEL_SLOT, ItemStack(Items.RED_CONCRETE).apply {
            set(DataComponents.ITEM_NAME, Component.literal("キャンセル"))
        })

        setItem(PREVIEW_SLOT, park.icon.copy().apply {
            set(DataComponents.ITEM_NAME, Component.literal(park.name))
            set(DataComponents.LORE, ItemLore(park.description.map { Component.literal(it).withStyle(Style.EMPTY.withItalic(false).withColor(0xFFFFFF)) }))
        })

        setOpenParentItem(22)
    }

    override fun onClick(packet: ServerboundContainerClickPacket) {
        mc.execute {
            val slot = packet.slotNum.toInt()
            when (slot) {
                BUY_SLOT -> {
                    val cost = park.cost
                    if (player.getMoney() < cost) {
                        player.sendSystemMessage(Component.literal("ポイントが足りません!"))
                        update(false)
                        player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.SPLASH_POTION_BREAK), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))
                        return@execute
                    }

                    if (playerPark.unlockedParks.contains(park)) {
                        player.sendSystemMessage(Component.literal("そのパークは既に購入しています!"))
                        update(false)
                        player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.SPLASH_POTION_BREAK), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))
                        return@execute
                    }

                    playerPark.unlockedParks.add(park)
                    player.addMoney(-cost)
                    player.sendSystemMessage(Component.literal("購入しました!"))
                    player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.PLAYER_LEVELUP), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))
                    openParent()
                }

                CANCEL_SLOT -> {
                    openParent()
                }

                else -> {
                    update()
                }
            }
        }
    }

    companion object {
        const val BUY_SLOT = 10
        const val CANCEL_SLOT = 16
        const val PREVIEW_SLOT = 13
    }
}