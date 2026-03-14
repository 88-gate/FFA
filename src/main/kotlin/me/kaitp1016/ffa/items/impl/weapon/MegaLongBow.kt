package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import net.minecraft.world.entity.projectile.arrow.Arrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import org.bukkit.Material
import java.util.*

object MegaLongBow: CustomItem() {
    override val id = "MEGA_LONG_BOW"
    override val name = "Mega Long Bow"
    override val material = Material.BOW
    override val rarity = Rarity.RARE
    override val category = ItemCategory.WEAPON

    const val COOLDOWN = 2f
    val cooldownLocation = Identifier.parse("ffa:mega_long_bow_cooldown")

    override fun createItem(amount: Int): org.bukkit.inventory.ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:bow"))
            set(DataComponents.USE_COOLDOWN, UseCooldown(COOLDOWN, Optional.of(cooldownLocation)) )
        }.bukkitStack
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        val player = event.player.asCraftPlayer().handle
        val level = player.level()
        val weapon = event.bukkitEvent.item!!.asCraftItemStack().handle
        if (player.cooldowns.isOnCooldown(weapon)) return

        val pickupItem = player.getProjectile(weapon)
        if (pickupItem.isEmpty) return

        player.cooldowns.addCooldown(cooldownLocation, COOLDOWN.toInt() * 20)
        pickupItem.count--

        val arrow = Projectile.spawnProjectileFromRotationDelayed(MegaLongBowArrow::new,level,weapon,player, 0f, 2.5f, 1.0f)
        if (arrow.attemptSpawn()) {
            arrow.projectile().apply {
                this.pickupItemStack = pickupItem

                if (player.hasInfiniteMaterials()) {
                    this.pickup = AbstractArrow.Pickup.CREATIVE_ONLY
                }
            }
        }
    }

    @ItemEventHandler
    fun onShoot(event: ItemEvents.ShootEvent) {
        event.isCancelled = true
    }

    class MegaLongBowArrow: Arrow {
        val shooter: ServerPlayer

        constructor(level: Level, player: LivingEntity, weapon:ItemStack):super(level,player, ItemStack.EMPTY,weapon) {
            this.shooter = player as ServerPlayer
        }

        override fun onHitEntity(result: EntityHitResult) {
            shooter.addEffect(MobEffectInstance(MobEffects.JUMP_BOOST,30,1))
            super.onHitEntity(result)
        }

        companion object {
            fun new(level: Level,shooter: LivingEntity,weapon: ItemStack): MegaLongBowArrow {
                return MegaLongBowArrow(level,shooter,weapon)
            }
        }
    }
}