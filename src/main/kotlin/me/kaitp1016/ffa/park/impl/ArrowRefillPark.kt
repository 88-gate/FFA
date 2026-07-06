package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.Material
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class ArrowRefillPark: Park() {
    override val icon = ItemStack(Items.ARROW)
    override val name = "Arrow Refill"
    override val cost = 7500
    override val description = listOf(
        "敵に矢でダメージを与えたら矢を数個獲得する。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        if (event.damageSource.damageType == DamageType.ARROW) {
            player.give(org.bukkit.inventory.ItemStack(Material.ARROW).apply {
                amount = 3
            })
        }
    }
}