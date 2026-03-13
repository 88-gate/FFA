package me.kaitp1016.ffa.items.impl.admin

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

object AspectOfTheVoid: CustomItem() {
    override val id = "ASPECT_OF_THE_VOID"
    override val name = "Aspect of the Void"
    override val material = Material.SUGAR_CANE
    override val rarity = Rarity.EPIC
    override val category = ItemCategory.ADMIN

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.ITEM_MODEL, Identifier.parse("minecraft:diamond_shovel"))
        }.bukkitStack
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        event.isCancelled = true

        val player = event.player
        if (player.isSneaking) {
            val world = player.world
            val raytrace = player.rayTraceBlocks(64.0) ?: return
            val block = raytrace.hitBlock!!

            if (!world.getBlockAt(block.x,block.y + 1,block.z).isReplaceable || !world.getBlockAt(block.x,block.y + 1,block.z).isReplaceable)  {
                return
            }

            val location = block.location.apply {
                this.add(0.5,1.002,0.5)
                this.yaw = player.yaw
                this.pitch = player.pitch
            }

            player.teleport(location)
            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_HURT,1f,0.5f)
        }
        else {
            val raytrace = player.rayTraceBlocks(12.0)
            if (raytrace == null) {
                val location = player.location.clone().apply {
                    this.add(direction.clone().multiply(12.0))
                    this.add(0.5,0.0,0.5)
                    this.yaw = player.yaw
                    this.pitch = player.pitch
                }

                player.teleport(location)
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT,1f,1f)
            }
            else {
                val block = raytrace.hitBlock!!
                val face = raytrace.hitBlockFace!!

                val location = Location(block.world,block.x.toDouble(),block.y.toDouble(),block.z.toDouble()).apply {
                    this.add(face.direction)
                    this.add(0.5,0.0,0.5)
                    this.yaw = player.yaw
                    this.pitch = player.pitch
                }

                player.teleport(location)
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT,1f,1f)
            }
        }
    }
}