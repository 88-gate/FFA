package me.kaitp1016.ffa.perk.extra.impl

import me.kaitp1016.ffa.perk.extra.ExtraPerk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute

class StreakLoverPerk: ExtraPerk() {
    override val icon = ItemStack(Items.PUFFERFISH)
    override val name = "Streak Lover"
    override val cost = 10000
    override val canDuplicate = false
    override val description = listOf(
        "Kill Streakが増えたときに確率でKill Streakが追加で増える。",
    )
}