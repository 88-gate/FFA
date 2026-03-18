package me.kaitp1016.ffa.game.mining

import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.ChestPacketGui
import me.kaitp1016.ffa.utils.DatapackAPI.addMoney
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration import net.minecraft.core.component.DataComponents
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemLore

class MiningChestContainer: ChestPacketGui {
    override val name = "鉱山のチェスト"
    override val displayName = Component.text("鉱山のチェスト")

    val chest: MiningChest
    var totalProfits = 0

    constructor(player: ServerPlayer, chest: MiningChest, parent: AbstractPacketGui? = null):super(player,27) {
        this.parent = parent
        this.chest = chest

        this.items.fill(ItemStack(Items.GRAY_STAINED_GLASS_PANE).apply {
            this.set(DataComponents.ITEM_NAME,Component.text("クリックして掘る!").toMCComponent())
        })

        if (chest.finder != player.uuid) {
            for (index in 0..<items.size) {
                if (Math.random() > 0.6f)  continue

                val reward = chest.rewards[index]

                this.items[index] = ItemStack(reward.item).apply {
                    this.set(DataComponents.ITEM_NAME,Component.text("ここはもう掘られているようだ...").toMCComponent())
                    this.set(DataComponents.LORE, ItemLore(listOf(Component.text("§e${reward.money} ポイント").decoration(TextDecoration.ITALIC,false).toMCComponent())))
                }
            }
        }
    }

    override fun onClick(packet: ServerboundContainerClickPacket) {
        val slot = packet.slotNum.toInt()
        val item = items[slot]
        if (item.item != Items.GRAY_STAINED_GLASS_PANE) {
            update()
            return
        }

        val reward = chest.rewards[slot]
        player.addMoney(reward.money)
        player.bukkitEntity.playSound(player.bukkitEntity.location,reward.sound,1f,1f)

        totalProfits += reward.money

        items[slot] = ItemStack(reward.item).apply {
            this.set(DataComponents.LORE, ItemLore(listOf(Component.text("§e${reward.money} ポイント").decoration(TextDecoration.ITALIC,false).toMCComponent())))
        }

        update()
    }

    override fun onClose() {
        player.sendSystemMessage(Component.text("§a鉱山のチェストから §e${totalProfits}ポイント §a手に入れた!").toMCComponent())

        super.onClose()
    }
}