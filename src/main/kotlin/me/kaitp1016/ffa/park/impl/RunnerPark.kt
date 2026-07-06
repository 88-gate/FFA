package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class RunnerPark: Park() {
    override val icon = ItemStack(Items.IRON_BOOTS)
    override val name = "Runner"
    override val cost = 2000
    override val description = listOf(
        "Combat Tagの時間が減る。",
    )
}