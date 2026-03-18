package me.kaitp1016.ffa.commands

import me.kaitp1016.ffa.game.mining.MiningChest
import me.kaitp1016.ffa.items.ItemManager
import me.kaitp1016.ffa.packetgui.impl.ItemListGui
import me.kaitp1016.ffa.packetgui.impl.TestSignGui
import me.kaitp1016.ffa.packetgui.impl.setting.SettingGui
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.Utils
import net.minecraft.core.BlockPos
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.json.simple.ItemList

object FFACommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§cプレイヤーのみがこのコマンドを実行できます")
            return true
        }

        if (args.isEmpty() || args.getOrNull(0) == null) return false

        if (args[0] == "give") {
            if (args.size <= 1) {
                sender.sendMessage("選択してね")
            }
            if (args.size >= 2) {
                val item = ItemManager.itemIdMap[args[1]]

                if (item == null) {
                    sender.sendMessage("§c不明なアイテムです")
                    return true
                }

                sender.inventory.addItem(item.createItem(args.getOrNull(2)?.toIntOrNull() ?: 1))
                sender.sendMessage("§a" + args[1] + " §bを与えました")
            }
        }

        if (args[0] == "test") {
            TestSignGui(sender.asCraftPlayer().handle).open()
        }

        if (args[0] == "minecontainertest") {
            MiningChest(BlockPos.ZERO,sender.uniqueId).openContainer(sender.asCraftPlayer().handle)
        }

        if (args[0] == "setting") {
            SettingGui(sender.asCraftPlayer().handle,null).open()
        }

        if (args[0] == "items") {
            ItemListGui(sender.asCraftPlayer().handle,null).open()
        }

        return true
    }

    override fun onTabComplete(sender: CommandSender, comand: Command, label: String, args: Array<String>): List<String?>? {
        val suggestions = ArrayList<String>()

        if (args.size == 1) {
            suggestions.add("give")
            suggestions.add("test")
            suggestions.add("items")
            suggestions.add("setting")
            suggestions.add("minecontainertest")
        }
        else if (args.getOrNull(0) == "give" && args.size == 2) {
            suggestions.addAll(ItemManager.itemIdMap.keys)
        }

        return Utils.filterAndSortMostMatch(suggestions,args.lastOrNull())
    }
}