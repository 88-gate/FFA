package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.Utils.consumeItem
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
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
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import java.util.*
import kotlin.math.max

object EmeraldBlade: CustomItem() {
    override val id = "EMERALD_BLADE"
    override val name = "エメラルドブレード"
    override val material = Material.NETHERITE_SWORD
    override val rarity = Rarity.LEGENDARY
    override val category = ItemCategory.WEAPON
    override val description = "インベントリにエメラルドブロックがある時\nのみこの武器でダメージを与えることが\n出来る。ダメージを与える時インベントリからエメラルドブロックを\n1個消費し、敵のHPを20減らす。この効果で1HP未満にすることはできない。\nクールダウンは120秒。"
    override val history = "おかねもちがさいきょうだ！"
    override val isUnique = true
    override val isEnchantable = false

    const val COOLDOWN = 120f
    val cooldownLocation = Identifier.parse("ffa:emerald_blade_cooldown")

    const val ABILITY_DAMAGE = 20.0
    const val MIN_HEALTH = 1.0

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin,"emerald_blade"),createItem(1)).apply {
                this.shape("Q E","ND ","HNQ")
                this.setIngredient('Q', Material.QUARTZ)
                this.setIngredient('E', Material.EMERALD_BLOCK)
                this.setIngredient('N', Material.NETHERITE_SCRAP)
                this.setIngredient('D', Material.DIAMOND_BLOCK)
                this.setIngredient('H', Material.PLAYER_HEAD)
            }
        )
    }

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(COOLDOWN, Optional.of(cooldownLocation)))
            this.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers(
                listOf(
                    ItemAttributeModifiers.Entry(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, -0.9, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND),
                )
            ))
            this.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:diamond_sword"))
            this.set(DataComponents.UNBREAKABLE, Unit.INSTANCE)
        }.bukkitStack
    }

    @ItemEventHandler
    fun onDamage(event: ItemEvents.DamageEntityEvent) {
        val item = event.item.asCraftItemStack().handle
        val player = event.player.asCraftPlayer().handle
        val target = event.bukkitEvent.entity

        if (target !is LivingEntity || player.cooldowns.isOnCooldown(item)) {
            event.isCancelled = true
            return
        }

        if (!event.player.consumeItem({ it.type == Material.EMERALD_BLOCK },amount = 1) ) {
            player.sendSystemMessage(Component.literal("エメラルドブロックを持っていません!"))
            event.isCancelled = true
            return
        }

        player.cooldowns.addCooldown(cooldownLocation,COOLDOWN.toInt() * 20)
        target.health = max(target.health - ABILITY_DAMAGE,MIN_HEALTH)
        event.player.world.playSound(event.player, Sound.ITEM_MACE_SMASH_AIR,2f,0f)
    }
}
