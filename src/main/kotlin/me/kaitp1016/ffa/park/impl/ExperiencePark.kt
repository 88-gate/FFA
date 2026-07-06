package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import me.kaitp1016.ffa.utils.NMSUtils.toMC
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent

class ExperiencePark: Park() {
    override val icon = ItemStack(Items.EXPERIENCE_BOTTLE)
    override val name = "Experience"
    override val cost = 15000
    override val description = listOf(
        "プレイヤーを倒したら追加で経験値を落とすようになる。",
    )

    override fun onKill(player: Player, event: EntityDeathEvent) {
        val target = event.entity
        if (target is Player) {
            val level = target.world.toMC()
            level.addFreshEntity(ExperienceOrb(level,target.x,target.y,target.z,10, null,null,null))
        }
    }
}