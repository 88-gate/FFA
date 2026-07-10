package me.kaitp1016.ffa.perk.extra.impl

import me.kaitp1016.ffa.perk.extra.ExtraPerk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute

class BloodBerserkerPerk: ExtraPerk() {
    override val icon = ItemStack(Items.NETHERITE_INGOT)
    override val name = "Blood Berserker"
    override val cost = 10000
    override val canDuplicate = false
    override val description = listOf(
        "バーサーカーのクールダウンが減少する。",
    )
}