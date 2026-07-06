package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute

class JumpBoostPark: Park() {
    override val icon = ItemStack(Items.RABBIT_FOOT)
    override val name = "Jump Boost"
    override val cost = 10000
    override val description = listOf(
        "ジャンプ力が増える。",
    )

    override fun getAttributes(): List<Pair<Attribute, Double>> {
        return listOf(Attribute.JUMP_STRENGTH to 0.2)
    }
}