package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.Scheduler
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.UseCooldown
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*
import kotlin.math.max

object Pratidhvani: CustomItem() {
    override val id = "PRATIDHVANI"
    override val name = "プラティドヴァニ"
    override val material = Material.IRON_SWORD
    override val rarity = Rarity.EPIC
    override val category = ItemCategory.WEAPON
    override val isUnique = false
    override val isEnchantable = true

    override val description = "右クリックで能力を使用する。\nソニックブームを直線上に発射し、当たった敵には\nソニックブームの上方向の移動距離に応じた\nノックバックとダメージを与える。\nクールダウンは40秒。"

    const val COOLDOWN = 40f
    val cooldownLocation = Identifier.parse("ffa:pratidhvani_cooldown")

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(COOLDOWN, Optional.of(cooldownLocation)))
            this.set(
                DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers(
                    listOf(
                        ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,9.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                        ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                    )
                )
            )
        }.bukkitStack
    }

    const val SONICBOOM_REACH = 100
    const val SONICBOOM_DISTANCE = 2.5
    const val SONICBOOM_RANGE = 4.0
    const val SONICBOOM_DAMAGE = 10.0

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        val item = event.item.asCraftItemStack().handle
        val player = event.player
        val mcPlayer = player.asCraftPlayer().handle

        if (mcPlayer.cooldowns.isOnCooldown(item)) return
        mcPlayer.cooldowns.addCooldown(cooldownLocation, COOLDOWN.toInt() * 20)

        val world = player.world

        world.playSound(player.location, Sound.ENTITY_WARDEN_SONIC_CHARGE,1f,1f)
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS,40,3,false,false))

        Scheduler.scheduleTask(40) {
            val pos = player.location.clone().add(0.0,player.eyeHeight,0.0)
            val direction = pos.direction.clone()
            val moveVec = direction.clone().multiply(SONICBOOM_DISTANCE)

            val hits = mutableListOf<LivingEntity>(player)

            for (i in 0..SONICBOOM_REACH) {
                pos.add(moveVec)

                world.spawnParticle(Particle.SONIC_BOOM,pos,1)
                if (i % 10 == 0) {
                    world.playSound(pos, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, 1f)
                }

                world.getNearbyLivingEntities(pos,SONICBOOM_RANGE) { !hits.contains(it) }.forEach {target ->
                    val multiplier = max(0.5 +(target.y - event.player.y) / 15.0 ,0.5)

                    target.damage(SONICBOOM_DAMAGE,player)
                    target.velocity = target.velocity.add(direction.clone().multiply(multiplier))
                    hits.add(target)
                }
            }
        }
    }
}