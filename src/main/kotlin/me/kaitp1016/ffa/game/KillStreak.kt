package me.kaitp1016.ffa.game

import me.kaitp1016.ffa.events.impl.UpdateActionBarEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.UUID

object KillStreak: Listener {
    data class KillStreak(var streak: Int, var bounty: Int = 0)

    val killStreaks = mutableMapOf<UUID,KillStreak>()

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.player
        val playerUUID = player.uniqueId
        val killer = event.damageSource.causingEntity as? Player

        if (killStreaks.contains(playerUUID)) {
            val killStreak = killStreaks[playerUUID]!!
            if (killStreak.bounty > 0) {
                if (killer == null) {
                    Bukkit.broadcast(Component.text("§5§l⚔賞金首 §r").append(player.name().color(NamedTextColor.YELLOW).append(Component.text(" は死んでしまった! §e§l${killStreak.streak} Kill Streak §r§cを失った!"))))
                }
                else {
                    Bukkit.broadcast(Component.text("§5§l⚔賞金首 §r").append(killer.name().color(NamedTextColor.YELLOW).append(Component.text(" は §e").append(player.name()).append(Component.text(" §cの §b§l${killStreak.streak} Kill Streak §r§cを終わらせた! ")))))
                }
            }
        }

        if (killer == null) return

        val streak = killStreaks.getOrPut(killer.uniqueId) { KillStreak(0) }
        streak.streak++


        if (streak.streak % 25 == 0) {
            player.isGlowing = true
            streak.bounty += 100
            Bukkit.broadcast(Component.text("§5§l⚔賞金首 §r").append(killer.name().color(NamedTextColor.YELLOW).append(Component.text(" §aは §b§l${streak.streak} Kill Streak §r§aに到達した! §6賞金 §l+100!"))))

            Bukkit.getOnlinePlayers().forEach {
                it.playSound(it, Sound.ENTITY_WITHER_SPAWN,1f,1f)
            }
        }
        else if (streak.streak % 5 == 0) {
            killer.sendMessage(Component.text("§a§l${streak.streak} Kill Streak §r§eに到達した!"))
        }
    }

    @EventHandler
    fun onActionBar(event: UpdateActionBarEvent) {
        val player = event.player
        val streak = killStreaks[player.uniqueId] ?: return

        if (streak.streak > 4) {
            event.addText("§4Streak§7: §c§l${streak.streak}")
        }
    }
}