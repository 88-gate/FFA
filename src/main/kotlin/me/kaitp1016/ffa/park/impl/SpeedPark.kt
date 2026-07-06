package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute

class SpeedPark: Park() {
    override val icon = ItemStack(Items.SUGAR)
    override val name = "Speed"
    override val cost = 10000
    override val description = listOf(
        "移動速度が上昇する。",
    )

    override fun getAttributes(): List<Pair<Attribute, Double>> {
        return listOf(
            Attribute.MOVEMENT_SPEED to 0.0125,
        )
    }
}