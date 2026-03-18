package me.kaitp1016.ffa.commands

import me.kaitp1016.ffa.game.CombatTag.hasCombatTag
import me.kaitp1016.ffa.mc
import me.kaitp1016.ffa.utils.DatapackAPI.addMoney
import me.kaitp1016.ffa.utils.DatapackAPI.getMoney
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.Utils
import net.minecraft.network.chat.Component
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

object PayCommand: CommandExecutor, TabCompleter {
    override fun onCommand(player: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (player !is Player) {
            player.sendMessage("このコマンドはプレイヤーしか実行できません!")
            return true
        }

        val target = args.getOrNull(0)?.let { mc.playerList.getPlayer(it) }
        if (target == null) {
            player.sendMessage("指定されたプレイヤーが見つかりませんでした。")
            return true
        }

        val amount = args.getOrNull(1)?.toIntOrNull()
        if (amount == null) {
            player.sendMessage("金額は数字のみを入力できます。")
            return true
        }

        if (player.asCraftPlayer().handle.getMoney() < amount) {
            player.sendMessage("自分の所持ポイント以上のお金を振り込めません。")
            return true
        }

        player.asCraftPlayer().handle.addMoney(-amount)
        target.addMoney(amount)

        player.sendMessage("§e${amount}ポイント §aを §b${target.plainTextName} §aに振り込みました!")
        target.sendSystemMessage(Component.literal("§e${amount}ポイント §aを §b${player.name} §aから受け取りました!"))

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String> {
        if (args.size < 2) return Utils.filterAndSortMostMatch(mc.playerNames.toList(),args.lastOrNull())

        return emptyList()
    }
}