package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.mc
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftEntity
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Unit
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityReference
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import net.minecraft.world.entity.projectile.arrow.ThrownTrident
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerExplosion
import net.minecraft.world.level.SimpleExplosionDamageCalculator
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import java.util.*

object OdinSpear: CustomItem() {
    override val id = "ODIN_SPEAR"
    override val name = "オーディンの槍"
    override val material = Material.TRIDENT
    override val rarity = Rarity.LEGENDARY
    override val category = ItemCategory.WEAPON
    override val description = "投げられたトライデントは\n当たった地点に爆発が起こり、\n使用者の手元に戻ってくる。\nクールダウンは30秒。"
    override val history = "最高神オーディンが使っていたと\nされている槍。ドワーフの職人に\nよって作られた聖なる槍であり、\n当たった位置に爆発が起こり\n持ち主の手元に戻ってくる。"
    override val isUnique = true
    override val isEnchantable = false

    const val DAMAGE_MULTIPLIER = 0.35f
    const val COOLDOWN = 30f

    val cooldownLocation = Identifier.parse("ffa:odin_spear_cooldown")

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(COOLDOWN, Optional.of(cooldownLocation)))
            this.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers(
                listOf(
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 11.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                )
            ))
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        }.bukkitStack.apply {
            this.addEnchantment(Enchantment.LOYALTY,3)
        }
    }

    @ItemEventHandler
    fun onTridentThrow(event: ItemEvents.TridentThrowEvent) {
        if (event.bukkitEvent.projectile.asCraftEntity().handle is ThrownOdinSpear) return

        val player = event.player.asCraftPlayer().handle
        val level = player.level()
        val item = event.bukkitEvent.itemStack.asCraftItemStack().handle ?: return

        event.isCancelled = true

        player.cooldowns.addCooldown(cooldownLocation, COOLDOWN.toInt() * 20)
        event.player.playSound(event.player, Sound.ITEM_TRIDENT_THROW,1f,1f)

        val odinSpear = Projectile.spawnProjectileFromRotationDelayed(ThrownOdinSpear::new,level,item,player, 0f, 2.5f, 1.0f)
        if (odinSpear.attemptSpawn()) {
            odinSpear.projectile().apply {
                this.owner = EntityReference.of(player)
                this.pickupItemStack = item

                if (player.hasInfiniteMaterials()) {
                    this.pickup = AbstractArrow.Pickup.CREATIVE_ONLY
                }
                else {
                    player.inventory.removeItem(item)
                }
            }
        }
    }

    class ThrownOdinSpear: ThrownTrident {
        val shooter: LivingEntity

        constructor(level: Level,shooter: LivingEntity,pickupItem: net.minecraft.world.item.ItemStack):super(level,shooter,pickupItem) {
            this.shooter = shooter
        }

        override fun onHitEntity(result: EntityHitResult) {
            explode()
        }

        override fun onHitBlock(result: BlockHitResult) {
            explode()
        }

        fun explode() {
            val explosion = OdinSpearExplosion(level() as ServerLevel,shooter,this, this.position())
            explosion.explode()

            val location = Location(level().world,this.position().x,this.position().y,position().z)
            level().world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER,2f,2f)
            level().world.spawnParticle(Particle.EXPLOSION_EMITTER,location,1,0.0,0.0,0.0)
        }

        companion object {
            fun new(level: Level,shooter: LivingEntity,pickupItem: net.minecraft.world.item.ItemStack): ThrownOdinSpear {
                return ThrownOdinSpear(level,shooter,pickupItem)
            }
        }

        class OdinSpearExplosion: ServerExplosion {
            private val level: ServerLevel
            private val player: LivingEntity
            private val trident: ThrownTrident
            private val center: Vec3

            constructor(level: ServerLevel,player: LivingEntity,trident: ThrownTrident,center: Vec3) : super(level,trident, DamageSource(explosionDamageType,trident,player,center), OdinSpearExplosionCalculator(),center,5f,false, Explosion.BlockInteraction.KEEP) {
                this.level = level
                this.player = player
                this.trident = trident
                this.center = center
            }

            override fun level(): ServerLevel {
                return this.level
            }


            override fun getBlockInteraction(): Explosion.BlockInteraction {
                return Explosion.BlockInteraction.KEEP
            }

            override fun getIndirectSourceEntity(): LivingEntity? {
                return player
            }

            override fun getDirectSourceEntity(): Entity? {
                return trident
            }

            override fun radius(): Float {
                return 5f
            }

            override fun center(): Vec3 {
                return center
            }

            override fun canTriggerBlocks(): Boolean {
                return false
            }

            override fun shouldAffectBlocklikeEntities(): Boolean {
                return false
            }

            override fun isSmall(): Boolean {
                return false
            }

            companion object {
                val explosionDamageType = mc.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.EXPLOSION)
            }

            class OdinSpearExplosionCalculator: SimpleExplosionDamageCalculator {
                constructor():super(false,true, Optional.of(1.5f), Optional.empty()) {

                }

                override fun getEntityDamageAmount(explosion: Explosion, entity: Entity, seenPercent: Float): Float {
                    return super.getEntityDamageAmount(explosion, entity, seenPercent) * DAMAGE_MULTIPLIER
                }
            }
        }
    }
}