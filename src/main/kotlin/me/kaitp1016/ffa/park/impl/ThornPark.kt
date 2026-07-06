package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import kotlin.random.Random

class ThornPark: Park() {
    override val icon = ItemStack(Items.CHAINMAIL_CHESTPLATE)
    override val name = "Thorn"
    override val cost = 3000
    override val description = listOf(
        "ダメージを受けたときに確率で固定量のダメージを反射する。",
    )

    override fun onDamage(player: Player, event: EntityDamageEvent) {
        if (Random.nextInt(0, 3) != 1) return
        val damager = event.damageSource.causingEntity as? LivingEntity ?: return
        damager.damage(2.0, DamageSource.builder(DamageType.THORNS).withDirectEntity(player).build())
    }
}