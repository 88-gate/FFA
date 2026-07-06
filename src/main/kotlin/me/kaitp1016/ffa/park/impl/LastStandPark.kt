package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.PLUGIN_ID
import me.kaitp1016.ffa.park.Park
import me.kaitp1016.ffa.utils.DatapackAPI.addMoney
import me.kaitp1016.ffa.utils.NMSUtils.toMC
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

class LastStandPark: Park() {
    override val icon = ItemStack(Items.PUFFERFISH)
    override val name = "Last Stand"
    override val cost = 5000
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