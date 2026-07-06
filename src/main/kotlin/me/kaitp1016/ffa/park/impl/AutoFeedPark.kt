package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.PLUGIN_ID
import me.kaitp1016.ffa.park.Park
import me.kaitp1016.ffa.utils.NMSUtils.toMC
import net.minecraft.core.Holder
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.entity.Player

class AutoFeedPark: Park() {
    override val icon = ItemStack(Items.COOKED_BEEF)
    override val name = "Auto Feed"
    override val cost = 5000
    override val description = listOf(
        "定期的に満腹度が自動で回復する。", "この効果は重複しない。"
    )

    override fun onTick(player: Player) {
        val player = player.toMC()
        val cooldowns = player.cooldowns
        if ((cooldowns.cooldowns[COOLDOWN_IDENTIFIER]?.endTime ?: 0) < cooldowns.tickCount) {
            player.foodData.eat(5, 0.5f)
            player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.PLAYER_BURP), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))
            cooldowns.addCooldown(COOLDOWN_IDENTIFIER, COOLDOWN)
        }
    }

    companion object {
        val COOLDOWN_IDENTIFIER = Identifier.fromNamespaceAndPath(PLUGIN_ID, "park_auto_feed_cooldown")
        const val COOLDOWN = 300
    }
}