package me.kaitp1016.ffa.items.impl.consumeable

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemUseAnimation
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.ResolvableProfile
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

object GoldenHead: CustomItem() {
    override val id = "GOLDEN_HEAD"
    override val name = "Golden Head"
    override val rarity = Rarity.RARE
    override val material = Material.GOLDEN_APPLE
    override val category = ItemCategory.CONSUMEABLE

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.PROFILE, ResolvableProfile.createUnresolved(UUID.fromString("ab467df5-5346-4ed5-a25e-d3435403cfb1")))
            this.set(DataComponents.ITEM_NAME,getDisplayName(this.bukkitStack).toMCComponent())
            this.set(DataComponents.FOOD, FoodProperties(4,4f,true))
            this.set(DataComponents.CONSUMABLE, Consumable(0f, ItemUseAnimation.NONE, Holder.direct(SoundEvents.PLAYER_BURP),false,listOf(ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.REGENERATION,60,1)),ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.ABSORPTION,1200,1)),ApplyStatusEffectsConsumeEffect(MobEffectInstance(MobEffects.SPEED,100,1)))))
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(10f, Optional.of(Identifier.parse("ffa:golden_head_cooldown"))))
            this.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:player_head"))
        }.bukkitStack
    }

    override fun getDisplayName(item: ItemStack?): Component {
        return Component.text(this.name).color(NamedTextColor.GOLD)
    }
}