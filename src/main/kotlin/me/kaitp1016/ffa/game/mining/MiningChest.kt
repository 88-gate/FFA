package me.kaitp1016.ffa.game.mining

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import org.bukkit.Sound
import java.util.*
import kotlin.random.Random

class MiningChest {
    data class MineReward(val money: Int,val item: Item,val sound: Sound)

    val finder: UUID
    val pos: BlockPos
    val screens = mutableMapOf<UUID, MiningChestContainer>()
    val rewards = List(27) { randomReward() }

    constructor(pos: BlockPos,finder:UUID) {
        this.pos = pos
        this.finder = finder
    }

    fun openContainer(player: ServerPlayer) {
        val screen = screens.getOrPut(player.uuid) { MiningChestContainer(player,this) }
        screen.open()
    }

    fun randomReward(): MineReward {
        val group = Random.nextInt(0,1000)
        if (group in 0..700) {
            return MineReward(Random.nextInt(0,5), Items.STONE,Sound.BLOCK_STONE_BREAK)
        }
        if (group in 700..890) {
            return MineReward(Random.nextInt(1,10), Items.DEEPSLATE,Sound.BLOCK_DEEPSLATE_BREAK)
        }
        if (group in 890..975) {
            return MineReward(Random.nextInt(10,20), Items.COAL,Sound.ENTITY_EXPERIENCE_ORB_PICKUP)
        }
        if (group in 975..990) {
            return MineReward(Random.nextInt(20,50), Items.IRON_INGOT,Sound.BLOCK_AMETHYST_BLOCK_PLACE)
        }
        if (group in 990..997) {
            return MineReward(Random.nextInt(50,300), Items.GOLD_INGOT,Sound.BLOCK_NOTE_BLOCK_PLING)
        }
        else {
            return MineReward(Random.nextInt(500,3000), Items.DIAMOND,Sound.ENTITY_ENDER_DRAGON_AMBIENT)
        }
    }
}