package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.PLUGIN_ID
import me.kaitp1016.ffa.perk.Perk
import me.kaitp1016.ffa.utils.NMSUtils.toMC
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

class LastStandPerk: Perk() {
    override val icon = ItemStack(Items.PUFFERFISH)
    override val name = "Last Stand"
    override val cost = 5000
    override val duplicateLimit = 1
    override val description = listOf(
        "体力が少なかったら時間経過で回復する。",
    )

    override fun onTick(player: Player) {
        val maxHealth = player.getAttribute(Attribute.MAX_HEALTH)?.value ?: return
        if (player.health / maxHealth < LAST_STAND_SCALE) {
            val player = player.toMC()
            val cooldowns = player.cooldowns
            if ((cooldowns.cooldowns[COOLDOWN_IDENTIFIER]?.endTime ?: 0) < cooldowns.tickCount) {
                player.heal(2f)
                cooldowns.addCooldown(COOLDOWN_IDENTIFIER, COOLDOWN)
            }
        }
    }

    companion object {
        const val LAST_STAND_SCALE = 0.3
        val COOLDOWN_IDENTIFIER = Identifier.fromNamespaceAndPath(PLUGIN_ID, "park_midas_cooldown")
        const val COOLDOWN = 80
    }
}