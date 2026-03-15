package me.kaitp1016.ffa.game.mining

import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.events.impl.UpdateActionBarEvent
import me.kaitp1016.ffa.utils.NMSUtils.asCraftBlock
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.objects.AtlasSprite
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.state.BlockState
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object MiningActionBarHandler: Listener {
    data class ActionBarData(val player:Player, val text: Component, var tick: Int = 100,val stroke: Int)
    var datas = mutableListOf<ActionBarData>()

    fun onMine(player: Player, block: Block, reward: Int, data: Mining.BlockData) {
        val original = datas.find { it.player == player }
        datas.remove(original)

        val stroke = original?.stroke?.plus(1) ?: 1
        val atlas = createAtlas(block.asCraftBlock().nms)
        val text = Component.literal("§7⛏").append(Component.`object`(atlas).append(Component.literal(" §e+${reward} §7(§a§l$stroke§7)")))

        datas.add(ActionBarData(player,text,stroke = stroke))
    }

    @EventHandler
    fun onTick(event: TickEvent) {
        datas.removeAll {
            it.tick--
            return@removeAll it.tick < 1
        }
    }

    @EventHandler
    fun onActionBar(event: UpdateActionBarEvent) {
        val data = datas.find { it.player == event.player } ?: return
        event.addComponent(data.text)
    }

    fun createAtlas(block: BlockState): AtlasSprite {
        val id = block.block.descriptionId.removePrefix("block.minecraft.")
        return AtlasSprite(Identifier.withDefaultNamespace("blocks"), Identifier.parse("block/$id"))
    }
}