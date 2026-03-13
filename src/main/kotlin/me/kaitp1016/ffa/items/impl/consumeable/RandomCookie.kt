package me.kaitp1016.ffa.items.impl.consumeable

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemUseAnimation
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.UseCooldown
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import java.util.Optional
import kotlin.random.Random

object RandomCookie: CustomItem() {
    override val id: String = "RANDOM_COOKIE"
    override val name = "ドキドキクッキー"
    override val rarity = Rarity.RARE
    override val material = Material.COOKIE
    override val category = ItemCategory.CONSUMEABLE

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.FOOD, FoodProperties(4,4f,true))
            this.set(DataComponents.CONSUMABLE, Consumable(3f, ItemUseAnimation.EAT, Holder.direct(SoundEvents.PLAYER_BURP),false,listOf()))
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(10f, Optional.of(Identifier.parse("ffa:random_cookie_cooldown"))))
        }.bukkitStack
    }

    @ItemEventHandler
    fun onConsume(event: ItemEvents.ConsumeEvent) {
        val effect = when(Random.nextInt(0,3)) {
            0 -> MobEffectInstance(MobEffects.SPEED,150,0)
            1 -> MobEffectInstance(MobEffects.SPEED,150,1)
            2 -> MobEffectInstance(MobEffects.SLOWNESS,150,0)
            3 -> MobEffectInstance(MobEffects.SLOWNESS,150,1)
            else -> throw IllegalStateException()
        }

        event.player.asCraftPlayer().handle.addEffect(effect)
    }

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin,"ramdom_cookie"), this.createItem(1)).apply {
                this.shape("SSS","SGS","SSS")
                this.setIngredient('S',Material.SUGAR)
                this.setIngredient('G',Material.GOLDEN_APPLE)
            },
        )
    }
}