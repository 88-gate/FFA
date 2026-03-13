package me.kaitp1016.ffa.packetgui.impl.setting

import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.ChestPacketGui
import me.kaitp1016.ffa.setting.SettingManager
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.core.component.DataComponents
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemLore

class SettingGui: ChestPacketGui {
    override val displayName = Component.text("設定")
    override val name = "設定"

    constructor(player: ServerPlayer,parent: AbstractPacketGui? = null):super(player,54) {
        this.parent = parent

        update()
        setOpenParentItem(49)
    }

    override fun onClick(packet: ServerboundContainerClickPacket) {
        val slot = packet.slotNum.toInt()
        val setting = SettingManager.settings.getOrNull(slot) ?: run {
            player.sendSystemMessage(Component.text("設定が見つかりませんでした!").toMCComponent())
            update()
            return
        }

        if (packet.buttonNum.toInt() == 1) {
            setting.reset()
            player.sendSystemMessage(Component.text("リセットしました!").toMCComponent())
            update()
        }
        else {
            val gui = SetValueGui(player,setting,this)
            gui.open()
        }
    }

    override fun update(reopen: Boolean) {
        var index = -1
        SettingManager.settings.forEach {setting ->
            index++

            val lore = mutableListOf<String>()
            lore.add("現在の値: ${setting.getValue()}")
            lore.add("デフォルト: ${setting.default}")
            lore.addAll(setting.description.split("\n"))

            this.setItem(index, ItemStack(Items.OAK_SIGN).apply {
                this.set(DataComponents.ITEM_NAME,Component.text(setting.name).toMCComponent())
                this.set(DataComponents.LORE, ItemLore(lore.map { Component.text(it).decoration(TextDecoration.ITALIC,false).color(NamedTextColor.WHITE).toMCComponent() }))
            })
        }

        super.update(reopen)
    }
}