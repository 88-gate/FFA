package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.Material
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class ArrowRefillPerk: Perk() {
    override val icon = ItemStack(Items.ARROW)
    override val name = "Arrow Refill"
    override val cost = 7500
    override val duplicateLimit = 4
    override val description = listOf(
        "敵に矢でダメージを与えたら矢を獲得する。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        if (event.damageSource.damageType == DamageType.ARROW) {
            player.give(org.bukkit.inventory.ItemStack.of(Material.ARROW).apply {
                amount = 3
            })
        }
    }
}