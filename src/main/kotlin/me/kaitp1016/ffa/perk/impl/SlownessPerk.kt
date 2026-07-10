package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

class SlownessPerk: Perk() {
    override val icon = ItemStack(Items.SNOW_BLOCK)
    override val name = "Slowness"
    override val cost = 2500
    override val description = listOf(
        "敵に近接ダメージを与えたら確率で敵の移動速度を低下させる。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        val source = event.damageSource
        if (Random.nextInt(0, 3) == 1 && source.damageType == DamageType.PLAYER_ATTACK) {
            val entity = event.entity as? LivingEntity
            entity?.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 100, 1))
        }
    }
}