package me.kaitp1016.ffa.packetgui.impl.kitselector

import net.minecraft.world.item.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

abstract class Kit {
    abstract val symbol: Item
    abstract val name: String
    abstract val description: String
    abstract val items: List<ItemStack>
    abstract val armors: Map<EquipmentSlot, ItemStack>

    fun give(player: Player,clearInventory: Boolean = false) {
        val inventory = player.inventory
        if (clearInventory) {
            inventory.clear()
        }

        armors.forEach {
            inventory.setItem(it.key,it.value.clone())
        }

        items.forEach {
            inventory.addItem(it)
        }
    }
}