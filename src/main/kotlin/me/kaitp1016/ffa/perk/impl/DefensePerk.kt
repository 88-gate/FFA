package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute

class DefensePerk: Perk() {
    override val icon = ItemStack(Items.IRON_CHESTPLATE)
    override val name = "Defense"
    override val cost = 5000
    override val duplicateLimit = 4
    override val description = listOf(
        "防御力が増える。",
    )

    override fun getAttributes(): List<Pair<Attribute, Double>> {
        return listOf(Attribute.ARMOR to 2.5)
    }
}