package me.kaitp1016.ffa.park

import me.kaitp1016.ffa.plugin
import net.minecraft.world.item.ItemStack
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent

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
}