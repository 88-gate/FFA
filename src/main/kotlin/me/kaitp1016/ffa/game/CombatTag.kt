package me.kaitp1016.ffa.game

import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.events.impl.UpdateActionBarEvent
import me.kaitp1016.ffa.park.ParkManager
import me.kaitp1016.ffa.park.Parks
import me.kaitp1016.ffa.setting.Settings
import me.kaitp1016.ffa.utils.NMSUtils.toMC
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
            player.toMC().kill(player.toMC().level())
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
            if (time + getCombatTagDuration(player) < currentTime) {
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

        if (time + getCombatTagDuration(this) < System.currentTimeMillis()) {
            combatTagStatus.remove(this)
            return false
        } else return true
    }

    fun Player.getCombatTagTime(): Long {
        val time = combatTagStatus[this] ?: return -1

        return time + getCombatTagDuration(this) - System.currentTimeMillis()
    }

    private fun getCombatTagDuration(player: Player): Int {
        val parks = ParkManager.getPark(player.uniqueId)
        val reduce = 1 - parks.selectedParks.count { it == Parks.RUNNER } * 0.1

        return (Settings.COMBAT_TAG_TIME.getValue() * reduce).toInt()
    }
}