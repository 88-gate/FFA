package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class WarriorPark: Park() {
    override val icon = ItemStack(Items.STONE_SWORD)
    override val name = "Warrior"
    override val cost = 100
    override val description = listOf(
        "与える近接ダメージが1.1倍になる。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        val source = event.damageSource
        if (source.damageType == DamageType.PLAYER_ATTACK) {
            event.damage * 1.1
        }
    }
}