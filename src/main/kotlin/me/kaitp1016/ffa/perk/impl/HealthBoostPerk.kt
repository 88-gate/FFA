package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute

class HealthBoostPerk: Perk() {
    override val icon = ItemStack(Items.APPLE)
    override val name = "Health Boost"
    override val cost = 5000
    override val description = listOf(
        "体力を追加で獲得する。",
    )

    override fun getAttributes(): List<Pair<Attribute, Double>> {
        return listOf(
            Attribute.MAX_HEALTH to 8.0,
        )
    }
}