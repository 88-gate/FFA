package me.kaitp1016.ffa.perk.extra.impl

import me.kaitp1016.ffa.perk.extra.ExtraPerk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute

class ToughnessPerk: ExtraPerk() {
    override val icon = ItemStack(Items.DIAMOND_CHESTPLATE)
    override val name = "Toughness"
    override val cost = 10000
    override val canDuplicate = false
    override val description = listOf(
        "防具強度が上がる",
    )

    override fun getAttributes(): List<Pair<Attribute, Double>> {
        return listOf(
            Attribute.ARMOR_TOUGHNESS to 5.0,
        )
    }
}