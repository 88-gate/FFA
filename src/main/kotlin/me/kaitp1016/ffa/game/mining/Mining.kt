package me.kaitp1016.ffa.game.mining

import it.unimi.dsi.fastutil.ints.IntList
import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.DatapackAPI.addMoney
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.NMSUtils.asCraftWorld
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.projectile.FireworkRocketEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.FireworkExplosion
import net.minecraft.world.item.component.Fireworks
import org.bukkit.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.server.PluginDisableEvent
import kotlin.random.Random

object Mining: Listener {
    data class BlockData(val money: () -> Int, val chestChance: Double, val respawnTick: Int,val bedrock:Boolean = true)

    val datas = mapOf(
        Material.COAL_ORE to BlockData(money = { Random.nextInt(0,3) }, chestChance = 0.005, respawnTick = 1200),
        Material.COPPER_ORE to BlockData(money = { Random.nextInt(1,2) }, chestChance = 0.005, respawnTick = 1200),
        Material.IRON_ORE to BlockData(money = { Random.nextInt(1,3) }, chestChance = 0.005, respawnTick = 1500),
        Material.LAPIS_ORE to BlockData(money = { Random.nextInt(0,5) }, chestChance = 0.007, respawnTick = 1200),
        Material.GOLD_ORE to BlockData(money = { Random.nextInt(2,4) }, chestChance = 0.007, respawnTick = 800),
        Material.DIAMOND_ORE to BlockData(money = { Random.nextInt(5,10) }, chestChance = 0.01, respawnTick = 1400),
        Material.DEEPSLATE_COAL_ORE to BlockData(money = { Random.nextInt(0,3) }, chestChance = 0.005, respawnTick = 1200),
        Material.DEEPSLATE_COPPER_ORE to BlockData(money = { Random.nextInt(1,2) }, chestChance = 0.005, respawnTick = 1200),
        Material.DEEPSLATE_IRON_ORE to BlockData(money = { Random.nextInt(1,3) }, chestChance = 0.005, respawnTick = 1500),
        Material.DEEPSLATE_LAPIS_ORE to BlockData(money = { Random.nextInt(0,5) }, chestChance = 0.007, respawnTick = 1200),
        Material.DEEPSLATE_GOLD_ORE to BlockData(money = { Random.nextInt(2,4) }, chestChance = 0.007, respawnTick = 800),
        Material.DEEPSLATE_DIAMOND_ORE to BlockData(money = { Random.nextInt(5,10) }, chestChance = 0.01, respawnTick = 1400),
        Material.AMETHYST_BLOCK to BlockData(money = { if (Random.nextInt(0,1) == 0) 0 else Random.nextInt(0,2) }, chestChance = 0.005, respawnTick = 2000, bedrock = false),
        Material.AMETHYST_CLUSTER to BlockData(money = { Random.nextInt(1,5) }, chestChance = 0.01, respawnTick = 2000, bedrock = true),
    )

    data class MinedPos(val pos: BlockPos, val world: World, val block: Material, val chest: MiningChest? = null, var tick: Int = 0) {
        fun respawn() {
            val pos = pos
            val location = Location(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
            world.playSound(location, Sound.ENTITY_CHICKEN_EGG, 2f, 0.5f)
            world.spawnParticle(Particle.CLOUD, location, 5, 0.6, 0.6, 0.6, 0.05)
            world.setBlockData(pos.x, pos.y, pos.z, block.createBlockData())
        }
    }

    val minedPos = mutableListOf<MinedPos>()

    @EventHandler(priority = EventPriority.LOWEST)
    fun onMine(event: BlockBreakEvent) {
        val block = event.block
        val pos = BlockPos(block.x, block.y, block.z)
        val player = event.player
        val data = datas[block.type] ?: return

        event.isCancelled = true
        val reward = data.money()

        val mcPlayer = player.asCraftPlayer().handle
        mcPlayer.mainHandItem.hurtAndBreak(1,mcPlayer, EquipmentSlot.MAINHAND)
        mcPlayer.addMoney(reward)

        MiningActionBarHandler.onMine(player, block, reward, data)


        val world = block.world
        if (Math.random() <= data.chestChance) {
            val chest = MiningChest(pos)
            minedPos.add(MinedPos(pos, world, block.type, chest, data.respawnTick))
            world.setBlockData(block.x, block.y, block.z, Material.CHEST.createBlockData())

            val level = world.asCraftWorld().handle
            val item = ItemStack(Items.FIREWORK_ROCKET).apply {
                this.set(DataComponents.FIREWORKS, Fireworks(1,listOf(FireworkExplosion(FireworkExplosion.Shape.STAR, IntList.of(255,255,255),IntList.of(0,255,255),true,false))))
            }

            level.addFreshEntity(FireworkRocketEntity(level,block.x.toDouble() + 0.5,block.y.toDouble() + 1,block.z.toDouble() + 0.5,item))
        }
        else {
            minedPos.add(MinedPos(pos, world, block.type, tick = data.respawnTick))
            val material = if (data.bedrock) Material.BEDROCK else Material.AIR
            world.setBlockData(block.x, block.y, block.z, material.createBlockData())
        }
    }

    @EventHandler
    fun onTick(event: TickEvent) {
        minedPos.removeAll {
            it.tick--
            if (it.tick > 1) {
                return@removeAll false
            }

            it.respawn()
            return@removeAll true
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        val block = event.clickedBlock ?: return
        if (block.type != Material.CHEST) return

        val pos = minedPos.find { it.chest != null && it.pos.x == block.x && it.pos.y == block.y && it.pos.z == block.z }
        if (pos == null) return

        event.isCancelled = true
        pos.chest!!.openContainer(event.player.asCraftPlayer().handle)
    }

    @EventHandler
    fun onDisable(event: PluginDisableEvent) {
        if (event.plugin != plugin) return
        minedPos.forEach {
            it.respawn()
        }
    }
}