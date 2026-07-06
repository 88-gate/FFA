package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class ResistancePark: Park() {
    override val icon = ItemStack(Items.IRON_INGOT)
    override val name = "Tank"
    override val cost = 5000
    override val description = listOf(
        "全ての受けるダメージと与えるダメージが0.7倍になる。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        event.damage *= 0.7
    }

    override fun onDamage(player: Player, event: EntityDamageEvent) {
        event.damage *= 0.7
    }
}