package me.kaitp1016.ffa.park

import io.papermc.paper.event.entity.EntityKnockbackEvent
import me.kaitp1016.ffa.plugin
import net.minecraft.world.item.ItemStack
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

abstract class Park {
    abstract val icon: ItemStack
    abstract val name: String
    abstract val description: List<String>
    abstract val cost: Int

    open fun register() {
        if (this is Listener) {
            plugin.server.pluginManager.registerEvents(this, plugin)
        }
    }

    open fun onHit(player: Player, event: EntityDamageByEntityEvent) {

    }

    open fun onKill(player: Player, event: EntityDeathEvent) {

    }

    open fun onDamage(player: Player, event: EntityDamageEvent) {

    }

    open fun onKnockback(player: Player, event: EntityKnockbackEvent) {

    }

    open fun onRespawn(player: Player, event: PlayerRespawnEvent) {

    }

    open fun onTick(player: Player) {

    }

    open fun getAttributes(): List<Pair<Attribute, Double>> {
        return emptyList()
    }
}