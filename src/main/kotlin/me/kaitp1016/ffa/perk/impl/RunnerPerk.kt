package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class RunnerPerk: Perk() {
    override val icon = ItemStack(Items.IRON_BOOTS)
    override val name = "Runner"
    override val cost = 2000
    override val duplicateLimit = 7
    override val description = listOf(
        "Combat Tagの時間が減る。",
    )
}