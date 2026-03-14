package me.kaitp1016.ffa.items.impl.admin

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.Utils.equalsOneOf
import net.minecraft.core.component.DataComponents
import net.minecraft.util.Unit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

object GrapplingHook: CustomItem(), Listener {
    override val id = "GRAPPLING_HOOK"
    override val name = "グラップリングフック"
    override val material = Material.FISHING_ROD
    override val rarity = Rarity.ADMIN
    override val category = ItemCategory.ADMIN

    private val coolDownPlayers = mutableListOf<Player>()

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        }.bukkitStack
    }

    @ItemEventHandler
    fun onFish(event: ItemEvents.FishEvent) {
        //空飛べたから修正
        if (event.bukkitEvent.state != PlayerFishEvent.State.IN_GROUND) return
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

        if (coolDownPlayers.contains(player)) {
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
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.entity !is Player || event.damager !is Player) return
        val player = event.entity as Player
        if (coolDownPlayers.contains(player)) return
        coolDownPlayers.add(player)
        object : BukkitRunnable() {
            override fun run() {
                coolDownPlayers.remove(player)
            }
        }.runTaskLater(plugin, 100L)
    }
}
