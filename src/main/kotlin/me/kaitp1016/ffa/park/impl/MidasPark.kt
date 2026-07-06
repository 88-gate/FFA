package me.kaitp1016.ffa.park.impl

import me.kaitp1016.ffa.park.Park
import me.kaitp1016.ffa.utils.DatapackAPI.addMoney
import me.kaitp1016.ffa.utils.NMSUtils.toMC
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class MidasPark: Park() {
    override val icon = ItemStack(Items.RAW_GOLD_BLOCK)
    override val name = "Midas"
    override val cost = 25000
    override val description = listOf(
        "敵にダメージを与えるとポイントを獲得する。",
    )

    override fun onHit(player: Player, event: EntityDamageByEntityEvent) {
        player.toMC().addMoney(2)
    }
}