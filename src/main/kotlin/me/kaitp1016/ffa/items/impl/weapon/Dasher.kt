package me.kaitp1016.ffa.items.impl.weapon

import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.NMSUtils.sendPacket
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket
import net.minecraft.resources.Identifier
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.level.block.Blocks
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import java.util.*

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
    val COOLDOWN_IDIENTIFIER = Identifier.parse("ffa:dasher_cooldown")

    // プレイヤーごとに前回のプレビュー位置を記録
    data class Preview(val uuid: UUID,val pos: BlockPos)
    private val lastPreviewPos = mutableListOf<Preview>()

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            this.set(DataComponents.USE_COOLDOWN, UseCooldown(COOLDOWN_TICKS / 20f, Optional.of(COOLDOWN_IDIENTIFIER)))
        }.bukkitStack
    }

    @ItemEventHandler
    fun onTick(event: ItemEvents.TickWhileHolding) {
        val player = event.player
        if (!player.isSneaking) return

        val raytrace = player.rayTraceBlocks(RANGE) ?: return
        val block = raytrace.hitBlock ?: return
        val world = block.world

        val above1 = world.getBlockAt(block.x, block.y + 1, block.z)
        val above2 = world.getBlockAt(block.x, block.y + 2, block.z)

        val canTeleport = above1.type.isAir && above2.type.isAir

        // ダイヤブロックのプレビューを送信
        val previewPos = BlockPos(block.x, block.y, block.z)
        val previewBlock = if (canTeleport) Blocks.DIAMOND_BLOCK else Blocks.REDSTONE_BLOCK

        player.sendPacket(ClientboundBlockUpdatePacket(previewPos, previewBlock.defaultBlockState()))
        lastPreviewPos.add(Preview(player.uniqueId,previewPos))
    }

    @ItemEventHandler
    fun onUse(event: ItemEvents.UseEvent) {
        event.isCancelled = true
        val player = event.player
        if (!player.isSneaking || player.hasCooldown(event.item)) return

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

        val location = block.location.add(0.5, 1.0, 0.5).apply {
            yaw = player.location.yaw
            pitch = player.location.pitch
        }

        player.teleport(location)
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
        player.asCraftPlayer().handle.cooldowns.addCooldown(COOLDOWN_IDIENTIFIER, COOLDOWN_TICKS)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onTick(event: TickEvent) {
        if (lastPreviewPos.isEmpty()) return

        lastPreviewPos.forEach { (uuid, pos) ->
            val player = Bukkit.getPlayer(uuid) ?: return@forEach
            val block = player.world.getBlockAt(pos.x, pos.y, pos.z)
            player.sendBlockChange(block.location, block.blockData)
        }

        lastPreviewPos.clear()
    }
}
