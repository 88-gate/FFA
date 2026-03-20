package me.kaitp1016.ffa.game

import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.events.impl.UpdateActionBarEvent
import me.kaitp1016.ffa.setting.Settings
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent

object CombatTag: Listener {
    val combatTagStatus = mutableMapOf<Player, Long>()

    @EventHandler(priority = EventPriority.MONITOR)
    fun onDamage(event: EntityDamageEvent) {
        if (event.isCancelled || event.damage <= 0.0) return

        val entity = event.entity as? Player ?: return
        val damager = event.damageSource.causingEntity as? Player ?: return

        combatTagStatus[damager] = System.currentTimeMillis()
        combatTagStatus[entity] = System.currentTimeMillis()
    }

    @EventHandler
    fun onLogout(event: PlayerQuitEvent) {
        val player = event.player
        if (player.hasCombatTag()) {
            player.asCraftPlayer().handle.kill(player.asCraftPlayer().handle.level())
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.player
        combatTagStatus.remove(player)
    }

    @EventHandler
    fun onTick(event: TickEvent) {
        val toRemove = mutableListOf<Player>()

        val currentTime = System.currentTimeMillis()

        combatTagStatus.forEach { (player, time) ->
            if (time + Settings.COMBAT_TAG_TIME.getValue() < currentTime) {
                toRemove.add(player)
            }
        }

        toRemove.forEach {
            combatTagStatus.remove(it)
        }
    }

    @EventHandler
    fun onActionBar(event: UpdateActionBarEvent) {
        val player = event.player
        val time = player.getCombatTagTime()
        if (time == -1L) return

        event.addText("§cCombat Tag§7: §e${time / 1000}秒")
    }


    fun Player.hasCombatTag(): Boolean {
        val time = combatTagStatus[this] ?: return false

        if (time + Settings.COMBAT_TAG_TIME.getValue() < System.currentTimeMillis()) {
            combatTagStatus.remove(this)
            return false
        }
        else return true
    }

    fun Player.getCombatTagTime(): Long {
        val time = combatTagStatus[this] ?: return -1

        return time + Settings.COMBAT_TAG_TIME.getValue() - System.currentTimeMillis()
    }
}