package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import net.minecraft.world.entity.projectile.arrow.Arrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import org.bukkit.Material

object GrapplingBow: CustomItem() {
    override val id = "GRAPPLING_BOW"
    override val material = Material.CROSSBOW
    override val name = "Grappling Crossbow"
    override val rarity = Rarity.RARE
    override val category = ItemCategory.WEAPON

    @ItemEventHandler
    fun onShoot(event: ItemEvents.ShootEvent) {
        val player = event.player.asCraftPlayer().handle
        val level = player.level()
        val weapon = event.bukkitEvent.bow!!.asCraftItemStack().handle
        val pickupItem = event.bukkitEvent.consumable?.asCraftItemStack()?.handle ?: ItemStack.EMPTY

        event.bukkitEvent.projectile.remove()

        val grapplingBowArrow = Projectile.spawnProjectileFromRotationDelayed(GrapplingBowArrow::new,level,weapon,player, 0f, 2.5f, 1.0f)
        if (grapplingBowArrow.attemptSpawn()) {
            grapplingBowArrow.projectile().apply {
                this.pickupItemStack = pickupItem

                if (player.hasInfiniteMaterials()) {
                    this.pickup = AbstractArrow.Pickup.CREATIVE_ONLY
                }
            }
        }
    }

    class GrapplingBowArrow: Arrow {
        val shooter: ServerPlayer

        constructor(level: Level, player: LivingEntity, weapon:ItemStack):super(level,player, ItemStack.EMPTY,weapon) {
            this.shooter = player as ServerPlayer
        }

        override fun onHitEntity(result: EntityHitResult) {
            val vec = this.position().subtract(shooter.position()).multiply(0.3,0.1,0.3).add(0.0,0.5,0.0)
            shooter.deltaMovement = vec
            shooter.hurtMarked = true

            this.remove(RemovalReason.DISCARDED)
        }

        companion object {
            fun new(level: Level,shooter: LivingEntity,weapon: ItemStack): GrapplingBowArrow {
                return GrapplingBowArrow(level,shooter,weapon)
            }
        }
    }
}