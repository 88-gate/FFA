package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.PLUGIN_ID
import me.kaitp1016.ffa.park.Park
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.DatapackAPI.addMoney
import me.kaitp1016.ffa.utils.NMSUtils.toMC
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.NamespacedKey
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.random.Random

class HealPark: Park() {
    override val icon = ItemStack(Items.CAKE)
    override val name = "Heal"
    override val cost = 5000
    override val description = listOf(
        "敵に近接ダメージを与えると確率で回復する。",
        "この効果はクールダウンがある。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        if (event.damageSource.damageType != DamageType.PLAYER_ATTACK || Random.nextInt(0,5) != 1) return

        val player = player.toMC()
        val cooldowns = player.cooldowns
        if ((cooldowns.cooldowns[COOLDOWN_IDENTIFIER]?.endTime ?: 0) < cooldowns.tickCount) {
            player.heal(4f)
            cooldowns.addCooldown(COOLDOWN_IDENTIFIER, COOLDOWN)
        }
    }

    companion object {
        val COOLDOWN_IDENTIFIER = Identifier.fromNamespaceAndPath(PLUGIN_ID, "park_heal_cooldown")
        const val COOLDOWN = 100
    }
}