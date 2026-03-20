package me.kaitp1016.ffa.items.impl.misc

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.DatapackAPI.getPrestige
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.Scheduler
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.component.UseCooldown
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

object SpeedSkill: CustomItem() {
    override val id = "SPEED_SIKLL"
    override val name = "Speed Skill"
    override val material = Material.SUGAR
    override val rarity = Rarity.EPIC
    override val category = ItemCategory.MISC
    override val description = "このアイテムを使用すると\n20秒間移動速度が10%\n上昇する。クールダウンは\n90秒。"

    const val COOLDOWN_TICK = 1800
    val COOLDOWN_IDENTIFIER = Identifier.parse("ffa:speed_skill_cooldown")
    val SPEED_SKILL_MODIFIER = Identifier.parse("ffa:w")

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(COOLDOWN_TICK / 20f, Optional.of(COOLDOWN_IDENTIFIER)))
        }.bukkitStack
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        val player = event.player.asCraftPlayer().handle
        val item = event.item.asCraftItemStack().handle
        if (player.cooldowns.isOnCooldown(item)) return

        if (player.getPrestige() < 5) {
            player.sendSystemMessage(Component.literal("このアイテムを使用するには Prestige 5以上が必要です!"))
            return
        }

        player.cooldowns.addCooldown(COOLDOWN_IDENTIFIER,COOLDOWN_TICK)

        val modifier = AttributeModifier(SPEED_SKILL_MODIFIER,0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
        player.getAttribute(Attributes.MOVEMENT_SPEED)!!.addTransientModifier(modifier)

        Scheduler.scheduleTask(400) {
            player.getAttribute(Attributes.MOVEMENT_SPEED)!!.removeModifier(modifier)
        }
    }
}