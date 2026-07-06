package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class EmptyPark: Park() {
    override val icon = ItemStack(Items.LIME_STAINED_GLASS_PANE)
    override val name = "未選択"
    override val cost = 0
    override val description = listOf(
        "未選択",
    )
}