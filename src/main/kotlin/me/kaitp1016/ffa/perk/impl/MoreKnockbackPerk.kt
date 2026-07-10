package me.kaitp1016.ffa.perk.impl

import io.papermc.paper.event.entity.EntityKnockbackEvent
import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.entity.Player

class MoreKnockbackPerk: Perk() {
    override val icon = ItemStack(Items.SNOWBALL)
    override val name = "More Knockback"
    override val cost = 1000
    override val duplicateLimit = 3
    override val description = listOf(
        "受けるノックバックが増加する",
    )

    override fun onKnockback(player: Player, event: EntityKnockbackEvent) {
        event.knockback = event.knockback.multiply(1.5)
    }
}