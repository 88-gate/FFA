package me.kaitp1016.ffa.items.impl.misc

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.item.component.UseCooldown
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

object SprintPack: CustomItem() {
    override val id = "SPRINT_PACK"
    override val name = "Sprint Pack"
    override val material = Material.POTION
    override val rarity = Rarity.RARE
    override val category = ItemCategory.MISC
    override val description = "右クリックをすると\n俊敏2を5秒間付与する。\nクールダウンは30秒。"

    val COOLDOWN_IDENTIFIER = Identifier.parse("ffa:sprint_pack")
    const val COOLDOWN = 600

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.POTION_CONTENTS, PotionContents(Optional.of(Potions.SWIFTNESS), Optional.empty<Int>(),emptyList<MobEffectInstance>(), Optional.empty<String>()))
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(COOLDOWN / 20f, Optional.of(COOLDOWN_IDENTIFIER)))
            this.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:tipped_arrow"))
            this.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay(false, linkedSetOf(DataComponents.POTION_CONTENTS)))
            this.set(DataComponents.CUSTOM_NAME, this@SprintPack.getDisplayName().toMCComponent())
        }.bukkitStack
    }

    override fun getDisplayName(item: ItemStack?): Component {
        return super.getDisplayName(item).decoration(TextDecoration.ITALIC,false)
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        event.isCancelled = true
        val player = event.player
        if (player.hasCooldown(event.item)) return

        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED,100,1))
        player.world.playSound(player.location, Sound.BLOCK_AMETHYST_BLOCK_PLACE,2f,0.9f)
        player.world.spawnParticle(Particle.GLOW,player.location,30,0.5,1.3,0.5)
        player.asCraftPlayer().handle.cooldowns.addCooldown(COOLDOWN_IDENTIFIER,COOLDOWN)
    }
}