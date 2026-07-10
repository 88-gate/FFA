package me.kaitp1016.ffa.perk.impl

import me.kaitp1016.ffa.perk.Perk
import me.kaitp1016.ffa.utils.NMSUtils.toMC
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

class DeathAdvantagePerk: Perk() {
    override val icon = ItemStack(Items.REDSTONE_BLOCK)
    override val name = "Death Advantage"
    override val cost = 10000
    override val duplicateLimit = 1
    override val description = listOf(
        "プレイヤーを殺した後にリスポーンした場合のみ",
        "最初から金のリンゴを所持している。",
    )

    override fun onKill(player: Player, event: EntityDeathEvent) {
        if (event.entity is Player) {
            player.toMC().tags.add("ffa_parks_early_advantage")
        }
    }

    override fun onRespawn(player: Player, event: PlayerRespawnEvent) {
        if (player.toMC().tags.remove("ffa_parks_early_advantage")) {
            player.give(ItemStack(Items.GOLDEN_APPLE).bukkitStack)
        }
    }
}