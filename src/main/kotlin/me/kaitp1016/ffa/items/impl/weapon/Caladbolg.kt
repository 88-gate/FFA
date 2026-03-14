package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.Scheduler
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.util.Unit
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.UseCooldown
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import java.util.*

object Caladbolg: CustomItem() {
    override val id = "CALADBOLG"
    override val name = "カラドボルグ"
    override val material = Material.NETHERITE_SWORD
    override val rarity = Rarity.LEGENDARY
    override val category = ItemCategory.WEAPON
    override val isUnique = true
    override val isEnchantable = false

    override val description = "右クリックで能力を使用する。\n10秒の間エンティティに対するリーチを2ブロック伸ばす。\nクールダウンは40秒。"
    override val history = "本来の力を発揮している時に\n眩い光を放つようになり、その状態で振られると\n刀が一瞬にして伸びるというもの。"

    const val COOLDOWN = 40f
    const val ABILITY_TIME = 10f
    const val ABILITY_TIME_PERCENT = 1f - (ABILITY_TIME / COOLDOWN)

    val cooldownLocation = Identifier.parse("ffa:caladbolg_cooldown")
    val entityInteractionRange = Identifier.parse("minecraft:entity_interaction_range")

    val defaultTexture = Identifier.parse("minecraft:diamond_sword")
    val poweredTexture = Identifier.parse("minecraft:golden_sword")

    val defaultModifier = ItemAttributeModifiers(
        listOf(
            ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 11.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
            ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
        )
    )

    val poweredModifier = ItemAttributeModifiers(
        listOf(
            ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 11.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
            ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
            ItemAttributeModifiers.Entry(Attributes.ENTITY_INTERACTION_RANGE, AttributeModifier(entityInteractionRange, 2.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
        )
    )

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(COOLDOWN, Optional.of(cooldownLocation)))
            this.set(DataComponents.ATTRIBUTE_MODIFIERS, defaultModifier)
            this.set(DataComponents.ITEM_MODEL,defaultTexture)
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        }.bukkitStack
    }

    override fun getRecipes(): List<Recipe> {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin,"caladbolg"),this.createItem(1)).apply {
                this.shape(" S ","ESE","RHR")
                this.setIngredient('S', Material.DIAMOND_SWORD)
                this.setIngredient('E', Material.ENDER_EYE)
                this.setIngredient('R', Material.REDSTONE_BLOCK)
                this.setIngredient('H', Material.PLAYER_HEAD)
            }
        )
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        val item = event.item.asCraftItemStack().handle
        val player = event.player.asCraftPlayer().handle

        if (!player.cooldowns.isOnCooldown(item)) {
            player.cooldowns.addCooldown(cooldownLocation,COOLDOWN.toInt() * 20)
            event.player.playSound(event.player.location, Sound.ITEM_TRIDENT_THUNDER,1f,2f)
            power(item)

            Scheduler.scheduleTask(ABILITY_TIME.toInt() * 20) {
                unpower(item)
                event.player.world.playSound(event.player, Sound.ITEM_TRIDENT_THROW,1f,0f)
            }
        }
    }

    @ItemEventHandler
    fun onTick(event: ItemEvents.TickWhileHolding) {
        val item = event.item.asCraftItemStack().handle
        val player = event.player.asCraftPlayer().handle

        val cooldown = player.cooldowns.getCooldownPercent(item,0f)
        if (cooldown < ABILITY_TIME_PERCENT) {
            unpower(item)
        }
    }

    fun power(item: net.minecraft.world.item.ItemStack) {
        item.set(DataComponents.ATTRIBUTE_MODIFIERS,poweredModifier)
        item.set(DataComponents.ITEM_MODEL,poweredTexture)
    }

    fun unpower(item:net.minecraft.world.item.ItemStack) {
        val attribute = item.get(DataComponents.ATTRIBUTE_MODIFIERS) ?: return

        if (attribute.modifiers().find { it.attribute == Attributes.ENTITY_INTERACTION_RANGE }?.modifier()?.amount == 2.0) {
            item.set(DataComponents.ATTRIBUTE_MODIFIERS, defaultModifier)
            item.set(DataComponents.ITEM_MODEL, defaultTexture)
        }
    }
}