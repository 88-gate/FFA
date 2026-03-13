package me.kaitp1016.ffa.items.impl.misc

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import net.kyori.adventure.text.Component
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AnvilMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.level.block.state.BlockState
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.minecraft.world.item.ItemStack as MCItemStack

object PortableAnvil: CustomItem() {
    override val id = "PORTABLE_ANVIL"
    override val name = "持ち運び式金床"
    override val rarity: Rarity = Rarity.RARE
    override val material = Material.ANVIL
    override val category = ItemCategory.MISC

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply{
            this.set(DataComponents.MAX_DAMAGE,16)
            this.set(DataComponents.DAMAGE,0)
            this.set(DataComponents.MAX_STACK_SIZE,1)
        }.bukkitStack
    }

    @ItemEventHandler
    fun onUsed(event: ItemEvents.UseEvent) {
        val player = event.player
        val item = event.item.asCraftItemStack().handle
        player.asCraftPlayer().handle.openMenu(AnvilWithStickMenu.getProvider(item))

        event.isCancelled = true
    }

    class AnvilWithStickMenu: AnvilMenu {
        val item: MCItemStack

        constructor(syncId:Int, inventory: Inventory, player: Player, item: MCItemStack) : super(syncId,inventory, ContainerLevelAccess.create(player.level(), BlockPos(player.x.toInt(),player.y.toInt(),player.z.toInt()))) {
            this.item = item
            this.checkReachable = false
        }

        override fun onTake(player: Player, stack: MCItemStack) {
            super.onTake(player, stack)
            item.hurtAndBreak(1, player, EquipmentSlot.MAINHAND)
            player.playSound(SoundEvents.ANVIL_USE, 1f, 1f)
        }

        override fun isValidBlock(state: BlockState): Boolean {
            return true
        }

        override fun stillValid(player: Player): Boolean {
            return player.inventory.hasAnyMatching { item == it } && item.damageValue < item.maxDamage
        }

        companion object {
            fun getProvider(item: MCItemStack): MenuProvider {
                return SimpleMenuProvider({syncId, inventory, player ->
                    AnvilWithStickMenu(syncId,inventory,player, item)
                },Component.text("金床").toMCComponent())
            }
        }
    }
}