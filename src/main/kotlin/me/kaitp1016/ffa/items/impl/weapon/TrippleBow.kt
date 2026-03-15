package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.items.impl.weapon.MegaLongBow.MegaLongBowArrow
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.Scheduler
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import net.minecraft.world.entity.projectile.arrow.Arrow
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.CrossbowItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import org.bukkit.Material
import org.bukkit.Sound
import java.util.*

object TrippleBow: CustomItem() {
    override val id = "TRIPPLE_BOW"
    override val name = "Triple Bow"
    override val material = Material.BOW
    override val rarity = Rarity.EPIC
    override val category = ItemCategory.WEAPON
    override val description = "この弓で矢を打つと追加で\n2連続の矢が出る。"

    @ItemEventHandler
    fun onShoot(event: ItemEvents.ShootEvent) {
        val player = event.player.asCraftPlayer().handle
        val weapon = event.bukkitEvent.bow!!.asCraftItemStack().handle

        val pickupItem = player.getProjectile(weapon)
        if (pickupItem.isEmpty) return

        val level = player.level()
        val power = event.bukkitEvent.force * 3f

        Scheduler.scheduleTask(5) {
            shootArrow(level, player, weapon,power)
            player.bukkitEntity.world.playSound(player.bukkitEntity.location,Sound.ENTITY_ARROW_SHOOT,2f,1.3f)
        }

        Scheduler.scheduleTask(10) {
            shootArrow(level, player, weapon,power)
            player.bukkitEntity.world.playSound(player.bukkitEntity.location,Sound.ENTITY_ARROW_SHOOT,2f,1.3f)
        }
    }

    fun shootArrow(level: ServerLevel, shooter: ServerPlayer, weapon: ItemStack, power: Float) {
        val arrow = Projectile.spawnProjectileFromRotationDelayed(::createArrow,level,weapon,shooter,1f,power,1f)

        if (arrow.attemptSpawn()) {
            arrow.projectile().apply {
                this.pickup = AbstractArrow.Pickup.CREATIVE_ONLY
            }
        }
    }

    fun createArrow(level: Level, shooter: LivingEntity, weapon: ItemStack): Arrow {
        return Arrow(level, shooter, ItemStack(Items.ARROW), weapon)
    }
}