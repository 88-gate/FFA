package me.kaitp1016.ffa.commands

import me.kaitp1016.ffa.game.CombatTag.hasCombatTag
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

object SpawnCommand: CommandExecutor, TabCompleter {
    override fun onCommand(player: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (player !is Player) {
            player.sendMessage("このコマンドはプレイヤーしか実行できません!")
            return true
        }

        if (player.hasCombatTag()) {
            player.sendMessage("Combat Tag中は使用できません!")
            return true
        }

        val location = player.respawnLocation ?: player.world.spawnLocation
        player.teleport(location)
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT,1f,1f)

        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String> {
        return emptyList()
    }
}