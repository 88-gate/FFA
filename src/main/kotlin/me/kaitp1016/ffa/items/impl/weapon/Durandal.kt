package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.util.Unit
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import kotlin.math.max

object Durandal: CustomItem() {
    override val id = "DURANDAL"
    override val name = "デュランダル"
    override val material = Material.NETHERITE_SWORD
    override val rarity = Rarity.LEGENDARY
    override val category = ItemCategory.WEAPON
    override val description = "非常に高い攻撃力を持つが、ダメージを与える時\n自身の体力が敵の体力を1上回るごとに-0.6ダメージ。\n更に、敵の体力が最大体力から1減るごとに-0.3ダメージ。\nこの効果でのダメージ減少量は最大-10ダメージ。"
    override val isUnique = true
    override val isEnchantable = false

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers(
                listOf(
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 19.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                )
            ))
            this.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:diamond_sword"))
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        }.bukkitStack
    }

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin,"durandal"), this.createItem(1)).apply {
                this.shape(" BN","EHB","DE ")
                this.setIngredient('B', Material.BLAZE_POWDER)
                this.setIngredient('N', Material.NETHERITE_SCRAP)
                this.setIngredient('H', Material.PLAYER_HEAD)
                this.setIngredient('E', Material.EMERALD)
                this.setIngredient('D', Material.DIAMOND_SWORD)
            }
        )
    }

    const val DAMAGE_REDUCE_PER_LOST_HEALTH = 0.6f
    const val DAMAGE_REDUCE_MULTIPLIER = 0.3f
    const val MAX_DAMAGE_REDUCE = 10.0

    @ItemEventHandler
    fun onDamage(event: ItemEvents.DamageEntityEvent) {
        val player = event.player
        val target = event.bukkitEvent.entity as? LivingEntity ?: return
        val maxHealth = target.getAttribute(Attribute.MAX_HEALTH)?.value ?: return
        val lostHealth = max(maxHealth - target.health,0.0)

        var damageReduce = max(player.health - target.health,0.0) * DAMAGE_REDUCE_MULTIPLIER + lostHealth * DAMAGE_REDUCE_PER_LOST_HEALTH
        if (damageReduce >= MAX_DAMAGE_REDUCE) damageReduce = MAX_DAMAGE_REDUCE

        if (event.bukkitEvent.isCritical) {
            damageReduce *= 1.5
        }

        event.bukkitEvent.damage -= damageReduce
    }
}