package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class PyroPark: Park() {
    override val icon = ItemStack(Items.BLAZE_POWDER)
    override val name = "Pyro"
    override val cost = 2500
    override val description = listOf(
        "敵に近接ダメージを与えたら確率で燃やす。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        val source = event.damageSource
        if (Random.nextInt(0, 3) == 1 && source.damageType == DamageType.PLAYER_ATTACK) {
            val entity = event.entity
            entity.fireTicks = max(entity.fireTicks, max(entity.fireTicks + 40, 100))
        }
    }
}