package me.kaitp1016.ffa.park

import me.kaitp1016.ffa.PLUGIN_ID
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import java.util.*

object AttributeParkHandler: Listener {
    fun update(player: Player) {
        val park = ParkManager.getPark(player.uniqueId)
        val attributes = mutableListOf<Pair<Attribute, Double>>()

        park.selectedParks.forEach { park ->
            attributes.addAll(park.getAttributes())
        }

        Registry.ATTRIBUTE.forEach { attribute ->
            val attribute = player.getAttribute(attribute)
            attribute?.modifiers?.toList()?.forEach { modifier ->
                val key = modifier.key
                if (key.namespace == PLUGIN_ID && key.key.startsWith("park_")) {
                    attribute.removeModifier(modifier)
                }
            }
        }

        attributes.forEach {
            player.getAttribute(it.first)?.addModifier(AttributeModifier(NamespacedKey(PLUGIN_ID,"park_${UUID.randomUUID()}"),it.second, AttributeModifier.Operation.ADD_NUMBER))
        }
    }

    @EventHandler
    fun onLogin(event: PlayerJoinEvent) {
        update(event.player)
    }

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        update(event.player)
    }
}