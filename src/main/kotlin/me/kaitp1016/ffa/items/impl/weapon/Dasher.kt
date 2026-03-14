package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.sendPacket
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket
import net.minecraft.world.level.block.Blocks
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import java.util.UUID

object Dasher: CustomItem(), Listener {
    override val id = "DASHER"
    override val name = "Blink"
    override val material = Material.PURPLE_DYE
    override val rarity = Rarity.RARE
    override val category = ItemCategory.WEAPON
    override val isEnchantable = false

    override val description = "右クリックで見ているブロックの上にテレポートする。\n上にブロックがある場合はテレポートできない。\n射程: 25ブロック\nクールダウン: 60秒"

    const val RANGE = 25.0
    const val COOLDOWN_TICKS = 60 * 20

    // プレイヤーごとに前回のプレビュー位置を記録
    private val lastPreviewPos = mutableMapOf<UUID, BlockPos>()

    @ItemEventHandler
    fun onTick(event: ItemEvents.TickWhileHolding) {
        val player = event.player
        clearPreview(player)

        if (player.hasCooldown(material)) return

        val raytrace = player.rayTraceBlocks(RANGE) ?: return
        val block = raytrace.hitBlock ?: return
        val world = block.world

        val above1 = world.getBlockAt(block.x, block.y + 1, block.z)
        val above2 = world.getBlockAt(block.x, block.y + 2, block.z)

        if (!above1.type.isAir || !above2.type.isAir) return

        // ダイヤブロックのプレビューを送信
        val previewPos = BlockPos(block.x, block.y, block.z)
        if (!player.isSneaking) return
        player.sendPacket(ClientboundBlockUpdatePacket(previewPos, Blocks.DIAMOND_BLOCK.defaultBlockState()))
        lastPreviewPos[player.uniqueId] = previewPos
    }

    private fun clearPreview(player: Player) {
        val pos = lastPreviewPos.remove(player.uniqueId) ?: return
        val block = player.world.getBlockAt(pos.x, pos.y, pos.z)
        player.sendBlockChange(block.location, block.blockData)
    }

    @EventHandler
    fun onItemHeld(event: PlayerItemHeldEvent) {
        clearPreview(event.player)
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        event.isCancelled = true
        val player = event.player

        if (!player.isSneaking) return
        if (player.hasCooldown(material)) return

        val raytrace = player.rayTraceBlocks(RANGE) ?: run {
            player.sendMessage("ブロックが見つかりません")
            return
        }

        val block = raytrace.hitBlock ?: return
        val world = block.world

        val above1 = world.getBlockAt(block.x, block.y + 1, block.z)
        val above2 = world.getBlockAt(block.x, block.y + 2, block.z)

        if (!above1.type.isAir || !above2.type.isAir) {
            player.sendMessage("テレポート先にスペースがありません")
            return
        }

        clearPreview(player)

        val location = block.location.add(0.5, 1.0, 0.5).apply {
            yaw = player.location.yaw
            pitch = player.location.pitch
        }

        player.teleport(location)
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
        player.setCooldown(material, COOLDOWN_TICKS)
    }
}
