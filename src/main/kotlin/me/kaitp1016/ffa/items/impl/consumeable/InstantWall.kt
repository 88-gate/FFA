package me.kaitp1016.ffa.items.impl.consumeable

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftBlock
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Blocks
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.event.block.Action
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.util.Vector
import kotlin.math.abs

object InstantWall: CustomItem() {
    override val id = "INSTANT_WALL"
    override val name = "即席壁"
    override val rarity = Rarity.UNCOMMON
    override val material = Material.GRAY_STAINED_GLASS
    override val category = ItemCategory.CONSUMEABLE

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin, "instant_wall"), this.createItem(1)).apply {
                this.shape("OGO","GSG","OGO")
                this.setIngredient('G', Material.GLASS)
                this.setIngredient('O', Material.OBSIDIAN)
                this.setIngredient('S', Material.STONE)
            },
        )
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        if (event.bukkitEvent.action != Action.RIGHT_CLICK_BLOCK || event.bukkitEvent.clickedBlock == null) return

        event.isCancelled = true

        val player = event.player.asCraftPlayer().handle
        val world = player.level() as ServerLevel

        if (!this.consumeOrMessage(event.player, amount = 1)) return

        val clickedBlock = event.bukkitEvent.clickedBlock!!
        val block = world.getBlockState(BlockPos(clickedBlock.x,clickedBlock.y,clickedBlock.z))
        val offset = if (block.canBeReplaced()) Vector(0,0,0) else event.bukkitEvent.blockFace.direction
        val basePos = event.bukkitEvent.clickedBlock!!.asCraftBlock().position.offset(offset.blockX,offset.blockY,offset.blockZ) ?: return
        val poses = mutableListOf<BlockPos>()

        if (abs(player.bukkitYaw % 180f) in 45f..135f) {
            repeat(9) {
                val x = basePos.x
                val y = it / 3 + basePos.y
                val z = basePos.z + it % 3 - 1

                poses.add(BlockPos(x, y, z))
            }
        }
        else {
            repeat(9) {
                val x = it % 3 - 1 + basePos.x
                val y = it / 3 + basePos.y
                val z = basePos.z

                poses.add(BlockPos(x, y, z))
            }
        }

        poses.forEach {pos ->
            val block = world.getBlockState(pos)
            if (block.isAir || block.canBeReplaced()) {
                world.setBlockAndUpdate(pos, Blocks.GRAY_STAINED_GLASS.defaultBlockState())
            }
        }

        event.player.world.playSound(event.player, Sound.BLOCK_ANVIL_PLACE,1f,1f)
    }
}