package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class ArcherPerk: Perk() {
    override val icon = ItemStack(Items.BOW)
    override val name = "Archer"
    override val cost = 3000
    override val duplicateLimit = 3
    override val description = listOf(
        "矢の与えるダメージが1.2倍になる。",
        "受けるダメージが1.5倍になる。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        if (event.damageSource.damageType == DamageType.ARROW) {
            event.damage *= 1.2
        }
    }

    override fun onDamage(player: Player, event: EntityDamageEvent) {
        event.damage *= 1.5
    }
}