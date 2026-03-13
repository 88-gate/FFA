package me.kaitp1016.ffa.game

import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.setting.Settings
import me.kaitp1016.ffa.utils.DatapackAPI.addMoney
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import kotlin.random.Random

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

            event.drops.clear()
            killer.world.playSound(killer.location,Sound.ENTITY_VINDICATOR_CELEBRATE,2f,1.15f)

            val reward = Settings.REVENGE_REWARD.getValue()
            killer.asCraftPlayer().handle.addMoney(reward)
            killer.sendMessage("§a復讐を成功して §e${reward}コイン §aを受け取りました!")

            val text = Component.empty().color(NamedTextColor.YELLOW).append(killer.name().append(Component.text(" は ").color(NamedTextColor.YELLOW).append(player.name()).append(Component.text(" に復讐を果たした!").color(NamedTextColor.YELLOW))))
            Bukkit.getOnlinePlayers().forEach {
                it.sendMessage(text)
            }
        }

        revenges.removeIf { it.player.uniqueId == player.uniqueId }

        revenges.add(RevengeTarget(player,killer, Settings.REVENGE_EXPIRE_TIME.getValue()))

        player.sendMessage(Component.text("あなたは ").color(NamedTextColor.YELLOW).append(killer.name().append(Component.text(" §rに復讐したいと感じた...").color(NamedTextColor.YELLOW))))
        killer.sendMessage(Component.text("あなたは ").color(NamedTextColor.YELLOW).append(player.name().append(Component.text(" §rから復讐したいという意思を感じ取った...").color(NamedTextColor.YELLOW))))
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        val source = event.damageSource
        val damager = source.causingEntity as? Player ?: return

        if (revenges.any { it.player.uniqueId == damager.uniqueId && it.killer.uniqueId == player.uniqueId }) {
            event.damage *= Settings.REVENGE_DAMAGE_MULTIPLIER.getValue()
            player.world.playSound(damager.location, Sound.ENTITY_RAVAGER_HURT,2f, 0.89f)
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