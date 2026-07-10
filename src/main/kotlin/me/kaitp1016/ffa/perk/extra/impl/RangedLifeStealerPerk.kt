package me.kaitp1016.ffa.perk.extra.impl

import me.kaitp1016.ffa.perk.extra.ExtraPerk
import me.kaitp1016.ffa.perk.impl.HealPerk.Companion.COOLDOWN
import me.kaitp1016.ffa.perk.impl.HealPerk.Companion.COOLDOWN_IDENTIFIER
import me.kaitp1016.ffa.utils.NMSUtils.toMC
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.attribute.Attribute
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import kotlin.random.Random

class RangedLifeStealerPerk: ExtraPerk() {
    override val icon = ItemStack(Items.RED_DYE)
    override val name = "Ranged Life Stealer"
    override val cost = 10000
    override val canDuplicate = false
    override val description = listOf(
        "矢を当てたときに自身の体力を回復する。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        if (event.damageSource.damageType != DamageType.ARROW) return

        player.heal(1.5)
    }
}