package me.kaitp1016.ffa.packetgui.impl

import me.kaitp1016.ffa.items.ItemManager
import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.ChestPacketGui
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import net.kyori.adventure.text.Component
import net.minecraft.core.component.DataComponents
import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class ItemListGui: ChestPacketGui {
    override val name = "Item List Gui"
    override val displayName get() = Component.text("アイテム一覧 (${currentPage + 1}/${maxPage + 1})")

    val itemList = ItemManager.items.copyOf()
    var currentPage = 0
    val maxPage get() = itemList.size / PAGE_SIZE

    constructor(player: ServerPlayer,parent: AbstractPacketGui?):super(player,54) {
        updateItems()
        setOpenParentItem(49)
    }

    override fun onClick(packet: ServerboundContainerClickPacket) {
        val slot = packet.slotNum.toInt()

        when(slot) {
            NEXT_PAGE_SLOT -> {
                if (maxPage > currentPage) {
                    currentPage++
                    updateItems()
                }
            }
            BACK_PAGE_SLOT -> {
                if (0 < currentPage) {
                    currentPage--
                    updateItems()
                }
            }
            else -> {
                if (slot <= PAGE_SIZE) {
                    val item = itemList.getOrNull(currentPage * (PAGE_SIZE + 1) + slot)
                    if (item == null) {
                        player.sendSystemMessage(Component.text("アイテムが見つかりませんでした!").toMCComponent())
                        update()
                        return
                    }

                    val slot = player.inventory.freeSlot
                    player.inventory.add(item.createItem(1).asCraftItemStack().handle)
                    player.connection.send(player.inventory.createInventoryUpdatePacket(slot))
                    player.sendSystemMessage(Component.text("§e${item.name} §rを受け取りました。").toMCComponent())
                }
            }
        }

        update()
    }

    fun updateItems() {
        val range = IntRange(currentPage * (PAGE_SIZE + 1), (currentPage + 1) * (PAGE_SIZE + 1))
        var slot = 0

        range.forEach {index ->
            if (index >= itemList.size) {
                this.setItem(slot, ItemStack.EMPTY)
            }
            else {
                val item = itemList[index]
                this.setItem(slot, item.createItem(1).asCraftItemStack().handle)
            }

            slot++
        }

        if (currentPage < maxPage) {
            this.setItem(NEXT_PAGE_SLOT,ItemStack(Items.ARROW).apply {
                this.set(DataComponents.ITEM_NAME,Component.text("次のページ").toMCComponent())
            })
        }
        else {
            this.setItem(NEXT_PAGE_SLOT,ItemStack.EMPTY)
        }

        if (currentPage > 0) {
            this.setItem(BACK_PAGE_SLOT,ItemStack(Items.ARROW).apply {
                this.set(DataComponents.ITEM_NAME,Component.text("前のページ").toMCComponent())
            })
        }
        else {
            this.setItem(BACK_PAGE_SLOT,ItemStack.EMPTY)
        }
    }
    companion object {
        const val PAGE_SIZE = 44
        const val NEXT_PAGE_SLOT = 53
        const val BACK_PAGE_SLOT = 45
    }
}