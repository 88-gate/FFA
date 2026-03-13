package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import net.minecraft.world.entity.EquipmentSlot
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity

object BeamSword: CustomItem() {
    override val id = "BEAM_SWORD"
    override val name = "Beam Sword"
    override val material = Material.IRON_SWORD
    override val rarity = Rarity.RARE
    override val category = ItemCategory.WEAPON

    const val BEAM_AMOUNMT = 150
    const val BEAM_DISTANCE = 0.3
    const val BEAM_DAMAGE = 2.0

    @ItemEventHandler
    fun onSwing(event: ItemEvents.SwingEvent) {
        val player = event.player
        val world = player.world
        val location = player.location.clone()
        val toAdd = location.direction.clone().multiply(BEAM_DISTANCE)
        location.add(0.0,player.eyeHeight,0.0)
        player.world.playSound(player.location, Sound.ITEM_DYE_USE,2f,2f)

        event.item.asCraftItemStack().handle.hurtAndBreak(1,player.asCraftPlayer().handle, EquipmentSlot.MAINHAND)

        var shouldBreak = false

        for (i in 0..BEAM_AMOUNMT) {
            location.add(toAdd)

            world.rayTraceBlocks(location,toAdd,BEAM_DISTANCE)?.hitBlock?.let {
                if (it.isCollidable) shouldBreak = true
            }
            if (shouldBreak) break

            world.rayTraceEntities(location,toAdd,BEAM_DISTANCE)?.hitEntity?.run {
                if (this == player) return@run
                val entity = this as? LivingEntity ?: return@run
                entity.damage(BEAM_DAMAGE,player)

                event.item.asCraftItemStack().handle.hurtAndBreak(1,player.asCraftPlayer().handle, EquipmentSlot.MAINHAND)
                shouldBreak = true
            }
            world.spawnParticle(Particle.END_ROD,location,0)

            if (shouldBreak) break
        }
    }
}