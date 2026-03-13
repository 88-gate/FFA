package me.kaitp1016.ffa.game

import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.setting.Settings
import me.kaitp1016.ffa.utils.DatapackAPI.addMoney
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent

object Revenge: Listener {
    data class RevengeTarget(val player: Player, val killer: Player, var tick: Int)

    val revenges = mutableListOf<RevengeTarget>()

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.player
        val source = event.damageSource
        val killer = source.causingEntity as? Player ?: return

        val revenge = revenges.find { it.player.uniqueId == killer.uniqueId && it.killer.uniqueId == player.uniqueId }
        if (revenge != null) {
            killer.inventory.clear()
            player.inventory.forEachIndexed { slot,item ->
                killer.inventory.setItem(slot,item)
            }

            player.inventory.clear()
            event.drops.clear()

            val reward = Settings.REVENGE_REWARD.getValue()
            player.asCraftPlayer().handle.addMoney(reward)
            player.sendMessage("§a復讐を成功して §e${reward}コイン §aを受け取りました!")

            val text = Component.text("").color(NamedTextColor.YELLOW).append(killer.name().append(Component.text(" は ").color(NamedTextColor.YELLOW).append(player.name()).append(Component.text(" に復讐を果たした!").color(NamedTextColor.YELLOW))))
            Bukkit.getOnlinePlayers().forEach {
                it.sendMessage(text)
            }
        }

        revenges.removeIf { it.player.uniqueId == player.uniqueId }

        revenges.add(RevengeTarget(player,killer, Settings.REVENGE_EXPIRE_TIME.getValue()))

        player.sendMessage(Component.text("あなたは ").color(NamedTextColor.YELLOW).append(player.name().append(Component.text(" §rに復讐したいと感じた...").color(NamedTextColor.YELLOW))))
        killer.sendMessage(Component.text("あなたは ").color(NamedTextColor.YELLOW) .append(player.name().append(Component.text(" §rから復讐したいという意思を感じ取った...").color(NamedTextColor.YELLOW))))
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        val source = event.damageSource
        val damager = source.causingEntity as? Player ?: return

        if (revenges.any { it.player.uniqueId == damager.uniqueId && it.killer.uniqueId == player.uniqueId }) {
            event.damage *= Settings.REVENGE_DAMAGE_MULTIPLIER.getValue()
        }
    }

    @EventHandler
    fun onTick(event: TickEvent) {
        revenges.removeAll { revenge ->
            revenge.tick--
            if (revenge.tick > 0) {
                return@removeAll false
            }

            revenge.player.sendMessage("§e復讐の意思を失ってしまった...")
            return@removeAll true
        }
    }
}