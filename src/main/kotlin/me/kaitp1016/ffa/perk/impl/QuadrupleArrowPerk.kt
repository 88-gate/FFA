package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class QuadrupleArrowPerk: Perk() {
    override val icon = ItemStack(Items.WHITE_DYE)
    override val name = "Quadruple Arrow"
    override val cost = 4444
    override val duplicateLimit = 1
    override val description = listOf(
        "Triple Bowで矢を発射すると矢を4本を発射するようになる。",
    )
}