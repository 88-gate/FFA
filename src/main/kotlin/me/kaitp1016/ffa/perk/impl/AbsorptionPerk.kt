package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class AbsorptionPerk: Perk() {
    override val icon = ItemStack(Items.GOLDEN_APPLE)
    override val name = "Absorption"
    override val cost = 5000
    override val duplicateLimit = 1
    override val description = listOf(
        "プレイヤーを倒したら衝撃吸収を獲得する。",
    )

    override fun onKill(player: Player, event: EntityDeathEvent) {
        val target = event.entity
        if (target is Player) {
            player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 200, 0))
        }
    }
}