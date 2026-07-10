package me.kaitp1016.ffa.perk.extra.impl

import me.kaitp1016.ffa.perk.extra.ExtraPerk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute

class BountifulStreakPerk: ExtraPerk() {
    override val icon = ItemStack(Items.GOLD_BLOCK)
    override val name = "Bountiful Berserk"
    override val cost = 10000
    override val canDuplicate = false
    override val description = listOf(
        "Kill Streakが100を超えてるときは25キルするごとに1000ポイント獲得する。",
    )
}