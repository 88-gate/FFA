package me.kaitp1016.ffa.items.impl.consumeable

import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.mc
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.DatapackAPI.getPrestige
import me.kaitp1016.ffa.utils.NMSUtils.asCraftBlock
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.NMSUtils.asCraftWorld
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.world.WorldUnloadEvent

object TemporaryBlocks: Listener {
    data class PlacedBlocks(val pos: BlockPos, val level: ResourceKey<Level>, val original: BlockState,var tick :Int) {
        fun remove() {
            val level = mc.getLevel(level)
            level?.setBlockAndUpdate(pos,original)
        }
    }

    val placedBlocks = mutableListOf<PlacedBlocks>()

    abstract class TemporaryBlock: CustomItem() {
        @ItemEventHandler
        fun onInteract(event: ItemEvents.UseEvent) {
            if (event.bukkitEvent.action != Action.RIGHT_CLICK_BLOCK) return
            event.isCancelled = true

            if (!onUse(event.player.asCraftPlayer().handle)) return

            val level = event.player.asCraftPlayer().handle.level()
            val face = event.bukkitEvent.blockFace
            val clickedBlock = event.bukkitEvent.clickedBlock ?: return
            val location = if (clickedBlock.isReplaceable) clickedBlock.location else event.bukkitEvent.clickedBlock?.location?.add(face.modX.toDouble(),face.modY.toDouble(),face.modZ.toDouble()) ?: return
            val blockPos = BlockPos(location.x.toInt(),location.y.toInt(),location.z.toInt())

            val currentBlock = level.getBlockState(blockPos)
            if (!currentBlock.canBeReplaced()) return

            if (!this.consumeOrMessage(event.player)) return

            onPlace(level,blockPos,currentBlock)
        }

        abstract fun onUse(player: ServerPlayer): Boolean

        abstract fun onPlace(level: Level,pos: BlockPos,currentBlock: BlockState)
    }

    object NormalTemporaryBlock: TemporaryBlock(){
        override val id = "TEMPORARY_BLOCK"
        override val name = "一時ブロック"
        override val material = Material.SPONGE
        override val rarity = Rarity.UNCOMMON
        override val category = ItemCategory.CONSUMEABLE
        override val description = "このアイテムは設置することができる。\n設置したブロックは20秒後に自動で消える。"

        const val BLOCK_DISAPPER_TICK = 400

        override fun onUse(player: ServerPlayer): Boolean {
            return true
        }

        override fun onPlace(level: Level, pos: BlockPos,currentBlock: BlockState ) {
            level.setBlockAndUpdate(pos, Blocks.SPONGE.defaultBlockState())
            placedBlocks.add(PlacedBlocks(pos,level.dimension(),currentBlock,BLOCK_DISAPPER_TICK))
        }
    }

    object WetTemporaryBlock: TemporaryBlock(){
        override val id = "WET_TEMPORARY_BLOCK"
        override val name = "濡れた一時ブロック"
        override val material = Material.WET_SPONGE
        override val rarity = Rarity.EPIC
        override val category = ItemCategory.CONSUMEABLE
        override val description = "このアイテムは設置することができる。\n設置したブロックは60秒後に自動で消える。"

        const val BLOCK_DISAPPER_TICK = 1200

        override fun onUse(player: ServerPlayer): Boolean {
            if (player.getPrestige() < 3) {
                player.sendSystemMessage(Component.literal("このアイテムを使用するには Prestige 4以上が必要です!"))
                return false
            }
            return true
        }

        override fun onPlace(level: Level, pos: BlockPos,currentBlock: BlockState ) {
            level.setBlockAndUpdate(pos, Blocks.WET_SPONGE.defaultBlockState())
            placedBlocks.add(PlacedBlocks(pos,level.dimension(),currentBlock,BLOCK_DISAPPER_TICK))
        }
    }

    @EventHandler
    fun onTick(event: TickEvent) {
        if (placedBlocks.isEmpty()) return

        placedBlocks.removeAll { block ->
            block.tick--
            if (block.tick > 1) return@removeAll false

            block.remove()
            return@removeAll true
        }
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