package me.kaitp1016.ffa.items.impl.misc

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.packetgui.impl.setting.SettingGui
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import org.bukkit.Material

object SettingBook: CustomItem() {
    override val id = "SETTING_BOOK"
    override val material = Material.BOOK
    override val name = "設定するための本"
    override val rarity = Rarity.ADMIN
    override val category = ItemCategory.MISC

    @ItemEventHandler
    fun onUsed(event: ItemEvents.UseEvent) {
        val player = event.player.asCraftPlayer().handle
        SettingGui(player,null).open()
    }
}