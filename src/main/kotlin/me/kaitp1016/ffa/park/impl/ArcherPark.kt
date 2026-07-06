package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class ArcherPark: Park() {
    override val icon = ItemStack(Items.BOW)
    override val name = "Archer"
    override val cost = 100
    override val description = listOf(
        "矢の与えるダメージが1.2倍になる。",
        "受けるダメージが1.5倍になる。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        if (event.damageSource.damageType == DamageType.ARROW) {
            event.damage *= 1.2
        }
    }

    override fun onDamage(player: Player, event: EntityDamageEvent) {
        event.damage *= 1.5
    }
}