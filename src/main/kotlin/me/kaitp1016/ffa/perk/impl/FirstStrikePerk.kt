package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class FirstStrikePerk: Perk() {
    override val icon = ItemStack(Items.GOLDEN_SWORD)
    override val name = "First Strike"
    override val cost = 5000
    override val duplicateLimit = 1
    override val description = listOf(
        "敵が最大体力の場合は与えるダメージが1.5倍になる。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        val target = event.entity as? LivingEntity ?: return
        val maxHealth = target.getAttribute(Attribute.MAX_HEALTH)?.value ?: return
        if (maxHealth <= target.health) {
            event.damage *= 1.5f
        }
    }
}