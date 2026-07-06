package me.kaitp1016.ffa.park

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent

object ParkListener: Listener {
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity is Player) {
            val park = ParkManager.getPark(entity.uniqueId)
            park.selectedParks.forEach { it.onDamage(entity, event) }
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val damager = event.damageSource.causingEntity
        if (damager is Player) {
            val park = ParkManager.getPark(damager.uniqueId)
            park.selectedParks.forEach { it.onHit(damager, event) }
        }
    }

    @EventHandler
    fun onDeath(event: EntityDeathEvent) {
        val damager = event.damageSource.causingEntity ?: return
        if (damager is Player) {
            val park = ParkManager.getPark(damager.uniqueId)
            park.selectedParks.forEach { it.onKill(damager, event) }
        }
    }
}