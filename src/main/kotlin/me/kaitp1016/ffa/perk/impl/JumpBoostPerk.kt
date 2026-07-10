package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute

class JumpBoostPerk: Perk() {
    override val icon = ItemStack(Items.RABBIT_FOOT)
    override val name = "Jump Boost"
    override val cost = 10000
    override val duplicateLimit = 4
    override val description = listOf(
        "ジャンプ力が増える。",
    )

    override fun getAttributes(): List<Pair<Attribute, Double>> {
        return listOf(Attribute.JUMP_STRENGTH to 0.2)
    }
}