package me.kaitp1016.ffa.items.impl.misc

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
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.component.UseCooldown
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import java.util.Optional

object Berserker: CustomItem() {
    override val id = "BERSERKER"
    override val name = "バーサーカー"
    override val material = Material.NETHERITE_INGOT
    override val description = "右クリックで使用する。\n使用したときに10秒間の俊敏2と15秒の間\nノックバック耐性がつき、基礎攻撃力が+2される。\nクールダウンは120秒。"
    override val rarity = Rarity.LEGENDARY
    override val category = ItemCategory.MISC
    override val isUnique = true

    const val COOLDOWN = 120f
    val cooldownLocation: Identifier = Identifier.parse("ffa:berserker_cooldown")

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(COOLDOWN, Optional.of(cooldownLocation)))
        }.bukkitStack
    }

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin,"berserker"),this.createItem(1)).apply {
                this.shape("DGD","GNG","SHB")
                this.setIngredient('D', Material.DIAMOND_BLOCK)
                this.setIngredient('G', Material.GOLD_INGOT)
                this.setIngredient('N', Material.NETHERITE_SCRAP)
                this.setIngredient('S', Material.SUGAR)
                this.setIngredient('H', Material.PLAYER_HEAD)
                this.setIngredient('B', Material.BLAZE_POWDER)
            }
        )
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        val player = event.player.asCraftPlayer().handle
        val item = event.item.asCraftItemStack().handle
        if (player.cooldowns.isOnCooldown(item)) return

        val bukkitPlayer = event.player

        bukkitPlayer.world.playSound(bukkitPlayer.location,Sound.ENTITY_RAVAGER_ROAR,1f,1f)
        player.cooldowns.addCooldown(cooldownLocation,COOLDOWN.toInt() * 20)

        player.getAttribute(Attributes.KNOCKBACK_RESISTANCE)!!.baseValue = 100.0
        player.getAttribute(Attributes.ATTACK_DAMAGE)!!.baseValue = 3.0
        player.addEffect(MobEffectInstance(MobEffects.SPEED,200,1))

        Scheduler.scheduleTask(300) {
            player.getAttribute(Attributes.KNOCKBACK_RESISTANCE)!!.baseValue = 0.0
            player.getAttribute(Attributes.ATTACK_DAMAGE)!!.baseValue = 1.0

            bukkitPlayer.world.playSound(bukkitPlayer.location,Sound.ENTITY_WOLF_SHAKE,1f,2f)
        }
    }
}