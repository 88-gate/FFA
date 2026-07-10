package me.kaitp1016.ffa.perk.extra.impl

import me.kaitp1016.ffa.perk.extra.ExtraPerk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class EmptyPerk: ExtraPerk() {
    override val icon = ItemStack(Items.LIME_STAINED_GLASS_PANE)
    override val name = "未選択"
    override val cost = 0
    override val canDuplicate = true
    override val description = listOf(
        "未選択",
    )
}