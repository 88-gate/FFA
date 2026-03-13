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
import net.minecraft.util.Unit
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.UseCooldown
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*
import kotlin.random.Random

object Excalibur: CustomItem() {
    override val id = "EXCALIBUR"
    override val name = "エクスカリバー"
    override val material = Material.NETHERITE_SWORD
    override val rarity = Rarity.LEGENDARY
    override val category = ItemCategory.WEAPON
    override val isUnique = true
    override val isEnchantable = false

    override val description = "右クリックで能力を使用する。\n自身に再生4を5秒間付与する。\nクールダウンは20秒。\nまた、この剣で殴られた敵には10%の確立で\n盲目を1.5秒間付与する。"
    override val history = "いつか歴史的な説明書く。"

    const val COOLDOWN = 20f
    val cooldownLocation = Identifier.parse("ffa:excalibur_cooldown")

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(COOLDOWN, Optional.of(cooldownLocation)))
            this.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers(
                listOf(
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 11.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                )
            ))
            this.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:diamond_sword"))
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        }.bukkitStack
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        val item = event.item.asCraftItemStack().handle
        val player = event.player.asCraftPlayer().handle

        if (!player.cooldowns.isOnCooldown(item)) {
            player.cooldowns.addCooldown(cooldownLocation,COOLDOWN.toInt() * 20)
            event.player.playSound(event.player, Sound.BLOCK_BEACON_ACTIVATE,1f,2f)
            player.addEffect(MobEffectInstance(MobEffects.REGENERATION,100,3))
        }
    }

    @ItemEventHandler
    fun onAttack(event: ItemEvents.DamageEntityEvent) {
        val target = event.bukkitEvent.entity as? Player ?: return
        if (Random.nextInt(0,9) == 0) target.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS,30,0,false,true,true))
    }
}