package me.kaitp1016.ffa.perk

import io.papermc.paper.event.entity.EntityKnockbackEvent
import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.perk.gui.PerkMainGui
import me.kaitp1016.ffa.utils.NMSUtils.toMC
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerRespawnEvent

object PerkListener: Listener {
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity is Player) {
            val perk = PerkManager.getPerkData(entity.uniqueId)
            perk.selectedPerks.forEach { it.onDamage(entity, event) }
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val damager = event.damageSource.causingEntity
        if (damager is Player) {
            val perk = PerkManager.getPerkData(damager.uniqueId)
            perk.selectedPerks.forEach { it.onHit(damager, event) }
        }
    }

    @EventHandler
    fun onDeath(event: EntityDeathEvent) {
        val damager = event.damageSource.causingEntity ?: return
        if (damager is Player) {
            val perk = PerkManager.getPerkData(damager.uniqueId)
            perk.selectedPerks.forEach { it.onKill(damager, event) }
        }
    }

    @EventHandler
    fun onKnockback(event: EntityKnockbackEvent) {
        val entity = event.entity ?: return
        if (entity is Player) {
            val perk = PerkManager.getPerkData(entity.uniqueId)
            perk.selectedPerks.forEach { it.onKnockback(entity, event) }
        }
    }

    @EventHandler
    fun onRespanw(event: PlayerRespawnEvent) {
        val player = event.player
        val perk = PerkManager.getPerkData(player.uniqueId)
        perk.selectedPerks.forEach { it.onRespawn(player, event) }
    }

    @EventHandler
    fun onTick(event: TickEvent) {
        Bukkit.getOnlinePlayers().forEach { player ->
            val perk = PerkManager.getPerkData(player.uniqueId)
            perk.selectedPerks.forEach { it.onTick(player) }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onInteract(event: PlayerInteractEntityEvent) {
        val entity = event.rightClicked
        if (!entity.toMC().tags.contains("ffa_park_gui")) return

        event.isCancelled = true
        PerkMainGui(event.player.toMC(), null).open()
    }
}