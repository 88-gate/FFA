package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute

class HealthBoostPark: Park() {
    override val icon = ItemStack(Items.APPLE)
    override val name = "Health Boost"
    override val cost = 5000
    override val description = listOf(
        "体力を追加で獲得する。",
    )

    override fun getAttributes(): List<Pair<Attribute, Double>> {
        return listOf(
            Attribute.MAX_HEALTH to 4.0,
        )
    }
}