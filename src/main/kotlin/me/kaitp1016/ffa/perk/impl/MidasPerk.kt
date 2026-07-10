package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.PLUGIN_ID
import me.kaitp1016.ffa.perk.Perk
import me.kaitp1016.ffa.utils.DatapackAPI.addMoney
import me.kaitp1016.ffa.utils.NMSUtils.toMC
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.random.Random

class MidasPerk: Perk() {
    override val icon = ItemStack(Items.RAW_GOLD_BLOCK)
    override val name = "Midas"
    override val cost = 25000
    override val duplicateLimit = 1
    override val description = listOf(
        "敵にダメージを与えるとポイントを獲得する。",
        "この効果はクールダウンがある。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        val player = player.toMC()
        val cooldowns = player.cooldowns
        if ((cooldowns.cooldowns[COOLDOWN_IDENTIFIER]?.endTime ?: 0) < cooldowns.tickCount) {
            player.addMoney(Random.nextInt(1, 12))
            cooldowns.addCooldown(COOLDOWN_IDENTIFIER, COOLDOWN)
        }
    }

    companion object {
        val COOLDOWN_IDENTIFIER = Identifier.fromNamespaceAndPath(PLUGIN_ID, "park_midas_cooldown")
        const val COOLDOWN = 50
    }
}