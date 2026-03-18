package me.kaitp1016.ffa.items.impl.consumeable

import me.kaitp1016.ffa.game.mining.Mining.minedPos
import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.mc
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftBlock
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.NMSUtils.asCraftWorld
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.world.WorldUnloadEvent
import java.util.UUID

object TemporaryBlock: CustomItem(), Listener {
    override val id = "TEMPORARY_BLOCK"
    override val name = "一時ブロック"
    override val material = Material.SPONGE
    override val rarity = Rarity.UNCOMMON
    override val category = ItemCategory.CONSUMEABLE
    override val description = "このアイテムは設置することができる。\n設置したブロックは初期装備の剣\nで壊すことができる。"

    data class PlacedBlocks(val pos: BlockPos, val level: ResourceKey<Level>, val original: BlockState) {
        fun remove() {
            val level = mc.getLevel(level)
            level?.setBlockAndUpdate(pos,original)
        }
    }

    val placedBlocks = mutableListOf<PlacedBlocks>()

    @ItemEventHandler
    fun onInteract(event: ItemEvents.UseEvent) {
        if (event.bukkitEvent.action != Action.RIGHT_CLICK_BLOCK) return
        event.isCancelled = true

        val level = event.player.asCraftPlayer().handle.level()
        val face = event.bukkitEvent.blockFace
        val clickedBlock = event.bukkitEvent.clickedBlock ?: return

        val location = if (clickedBlock.isReplaceable) clickedBlock.location else event.bukkitEvent.clickedBlock?.location?.add(face.modX.toDouble(),face.modY.toDouble(),face.modZ.toDouble()) ?: return
        val blockPos = BlockPos(location.x.toInt(),location.y.toInt(),location.z.toInt())

        val currentBlock = level.getBlockState(blockPos)
        if (!currentBlock.canBeReplaced()) return

        val world = event.player.world
        val item = event.item.type
        if (!this.consumeOrMessage(event.player)) return

        world.setBlockData(blockPos.x,blockPos.y,blockPos.z, Bukkit.createBlockData(item))
        placedBlocks.add(PlacedBlocks(blockPos,level.dimension(),currentBlock))
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onBreak(event: BlockBreakEvent) {
        val block = event.block.asCraftBlock()
        val pos = block.position
        val level = block.craftWorld.handle
        val levelkey = level.dimension()
        val placedBlock = placedBlocks.find { it.pos == pos && levelkey == it.level } ?: return

        event.isCancelled = true
        placedBlock.remove()
        placedBlocks.remove(placedBlock)
    }

    @EventHandler
    fun onWorldUnload(event: WorldUnloadEvent) {
        val level = event.world.asCraftWorld().handle.dimension()
        placedBlocks.removeAll {
            if (it.level == level) {
                it.remove()
                return@removeAll true
            }
            return@removeAll false
        }
    }

    @EventHandler
    fun onDisable(event: PluginDisableEvent) {
        if (event.plugin != plugin) return

        placedBlocks.forEach { block ->
            block.remove()
        }
    }
}