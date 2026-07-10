package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class EmptyPerk: Perk() {
    override val icon = ItemStack(Items.LIME_STAINED_GLASS_PANE)
    override val name = "未選択"
    override val cost = 0
    override val description = listOf(
        "未選択",
    )
}