package me.kaitp1016.ffa.items.impl.misc

import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.Utils.equalsOneOf
import net.minecraft.core.component.DataComponents
import net.minecraft.util.Unit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import java.util.*

object GrapplingHook: CustomItem(), Listener {
    override val id = "GRAPPLING_HOOK"
    override val name = "グラップリングフック"
    override val material = Material.FISHING_ROD
    override val rarity = Rarity.EPIC
    override val category = ItemCategory.MISC

    data class CombatCooldown(val uuid: UUID, var cooldown: Int)

    const val COMBAT_COOLDOWN = 100
    private val cooldowns = mutableListOf<CombatCooldown>()

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        }.bukkitStack
    }

    @ItemEventHandler
    fun onFish(event: ItemEvents.FishEvent) {
        if (!event.bukkitEvent.state.equalsOneOf(PlayerFishEvent.State.IN_GROUND, PlayerFishEvent.State.REEL_IN)) return

        // フックが地面付近にあるかチェック（バウンド対策）
        val hookLoc = event.bukkitEvent.hook.location
        val hasNearbyBlock = (-1..1).any { dx ->
            (-1..1).any { dy ->
                (-1..1).any { dz ->
                    val block = hookLoc.clone().add(dx * 0.1, dy * 0.1, dz * 0.1).block
                    block.type.isSolid
                }
            }
        }
        if (!hasNearbyBlock) return

        val player = event.player

        if (player.fireTicks > 0) {
            player.sendMessage("燃えている時はフックは利用できません")
            event.isCancelled = true
            return
        }

        if (player.hasPotionEffect(PotionEffectType.SLOWNESS)) {
            player.sendMessage("移動速度低下がついているときはフックは利用できません")
            event.isCancelled = true
            return
        }

        val uuid = player.uniqueId
        if (cooldowns.any { it.uuid == uuid }) {
            player.sendMessage("殴られた直後は使えません。")
            event.isCancelled = true
            return
        }

        val vel = event.bukkitEvent.hook.location.clone().subtract(player.location).toVector().normalize().multiply(2)
        if (vel.y <= 1) {
            vel.y = 0.1
        }
        player.velocity = vel
    }

    @EventHandler
    fun onTick(event: TickEvent) {
        cooldowns.removeAll {
            it.cooldown--
            return@removeAll it.cooldown < 1
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val source = event.damageSource
        val damager = source.causingEntity as? Player ?: return
        val player = event.entity as? Player ?: return

        setCooldown(player)
        setCooldown(damager)
    }

    fun setCooldown(player: Player) {
        val uuid = player.uniqueId

        val cooldown = cooldowns.find { it.uuid == uuid }
        if (cooldown == null) {
            cooldowns.add(CombatCooldown(uuid,COMBAT_COOLDOWN))
        }
        else {
            cooldown.cooldown = COMBAT_COOLDOWN
        }
    }
}
