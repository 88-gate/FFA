package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.asCraftEntity
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.Scheduler
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Unit
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LightningBolt
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileDeflection
import net.minecraft.world.entity.projectile.arrow.AbstractArrow
import net.minecraft.world.entity.projectile.arrow.ThrownTrident
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.random.Random

object Keraunos: CustomItem(), Listener {
    override val id = "KERANUNOS"
    override val name = "ケラウノス"
    override val material = Material.TRIDENT
    override val rarity = Rarity.LEGENDARY
    override val category = ItemCategory.WEAPON
    override val description = "当たった地点から4ブロックのランダムな位置に\n雷を15回召喚する。\nクールダウンは40秒。"
    override val history = "ケラウノスは雷のエネルギーを凝縮させて\n作られたとされている。その槍には\n山を砕き、海一面を沸騰させてしまうほどの\n雷を降らすエネルギーが込められている。"
    override val isUnique = true
    override val isEnchantable = false

    const val THUNDER_DAMAGE_MULTIPLIER = 2.0
    const val THUNDER_AMOUNTS = 15
    const val THUNDER_SPREAD_DISTANCE = 4.0
    const val COOLDOWN = 40f
    val cooldownLocation = Identifier.parse("ffa:keranunos_cooldown")

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(COOLDOWN, Optional.of(cooldownLocation)))
            this.set(
                DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers(
                    listOf(
                        ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 11.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                        ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                    )
                )
            )
            this.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:trident"))
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        }.bukkitStack.apply {
            this.addEnchantment(Enchantment.LOYALTY,3)
        }
    }

    @ItemEventHandler
    fun onTridentThrow(event: ItemEvents.TridentThrowEvent) {
        if (event.bukkitEvent.projectile.asCraftEntity().handle is ThrownKeraunos) return

        val player = event.player.asCraftPlayer().handle
        val level = player.level()
        val item = event.bukkitEvent.itemStack.asCraftItemStack().handle ?: return

        event.isCancelled = true

        player.cooldowns.addCooldown(cooldownLocation, COOLDOWN.toInt() * 20)
        event.player.playSound(event.player, Sound.ITEM_TRIDENT_THROW,1f,1f)

        val thrownKeraunos = Projectile.spawnProjectileFromRotationDelayed(ThrownKeraunos::new,level,item,player, 0f, 2.5f, 1.0f)
        if (thrownKeraunos.attemptSpawn()) {
            thrownKeraunos.projectile().apply {
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

    @EventHandler
    fun onDamage(event: EntityDamageByEntityEvent) {
        if (event.damager.asCraftEntity().handle is ThrownKeraunos.KeraunosLightningBolt) {
            event.damage *= THUNDER_DAMAGE_MULTIPLIER
        }
    }

    class ThrownKeraunos: ThrownTrident {
        var spawned = false

        constructor(level: Level, shooter: LivingEntity, pickupItem: net.minecraft.world.item.ItemStack) : super(level, shooter, pickupItem) {

        }

        override fun onHitEntity(result: EntityHitResult) {
            spawnThunderRain()

            this.deflect(ProjectileDeflection.REVERSE, result.entity, this.owner, false)
            this.deltaMovement = this.deltaMovement.multiply(0.02, 0.2, 0.02)
            this.playSound(SoundEvents.TRIDENT_HIT, 1.0f, 1.0f)
        }

        override fun onHitBlock(result: BlockHitResult) {
            spawnThunderRain()
        }

        fun spawnThunderRain() {
            if (spawned) return
            spawned = true

            val world = level()
            val pos = position()

            repeat(THUNDER_AMOUNTS) {
                Scheduler.scheduleTask(it) {
                    val offsetX = Random.nextDouble(-THUNDER_SPREAD_DISTANCE,THUNDER_SPREAD_DISTANCE)
                    val offsetZ = Random.nextDouble(-THUNDER_SPREAD_DISTANCE,THUNDER_SPREAD_DISTANCE)

                    val thunder = KeraunosLightningBolt(EntityType.LIGHTNING_BOLT,level()).apply {
                        this.setPos(pos.add(offsetX,0.0,offsetZ))
                        this.flashes = 1
                    }

                    world.addFreshEntity(thunder)
                }
            }
        }

        companion object {
            fun new(level: Level, shooter: LivingEntity, pickupItem: net.minecraft.world.item.ItemStack): ThrownKeraunos {
                return ThrownKeraunos(level, shooter, pickupItem)
            }
        }

        class KeraunosLightningBolt: LightningBolt {
            constructor(type: EntityType<out LightningBolt>,level: Level):super(type,level) {

            }
        }
    }
}
