package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class AbsorptionPark: Park() {
    override val icon = ItemStack(Items.GOLDEN_APPLE)
    override val name = "Absorption"
    override val cost = 5000
    override val description = listOf(
        "プレイヤーを倒したら衝撃吸収を短時間獲得する。",
    )

    override fun onKill(player: Player, event: EntityDeathEvent) {
        val target = event.entity
        if (target is Player) {
            player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 200, 0))
        }
    }
}