package me.kaitp1016.ffa.items.impl.consumeable

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minecraft.world.item.enchantment.Enchantments
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import kotlin.jvm.optionals.getOrNull

object Overenchant: CustomItem() {
    override val id = "OVERENCAHNT"
    override val name = "オーバーエンチャント"
    override val material = Material.ENCHANTED_BOOK
    override val rarity = Rarity.RARE
    override val category = ItemCategory.CONSUMEABLE
    override val description = "インベントリでアイテムと入れ替えると\n表示順で一番上のエンチャントのレベルが\n1上昇する。最大レベルは超えない。"

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapedRecipe(NamespacedKey(plugin, "overenchant"), this.createItem(1)).apply {
                this.shape("BBB","ESE")
                this.setIngredient('B', Material.BOOK)
                this.setIngredient('E', Material.EMERALD)
                this.setIngredient('S', Material.EMERALD_BLOCK)
            },
        )
    }

    @ItemEventHandler
    fun onSwap(event: ItemEvents.SwapItemInInventoryEvent) {
        val item = event.bukkitEvent.currentItem?.asCraftItemStack()?.handle ?: return
        val enchantSet = item.enchantments.keySet().firstOrNull() ?: return fail(event,"そのアイテムにはエンチャントがついていません!")

        val enchant = enchantSet.value()
        val level = item.enchantments.getLevel(enchantSet)
        val maxLevel = when(enchantSet.unwrapKey().getOrNull()) {
            Enchantments.SHARPNESS -> 3
            Enchantments.PROTECTION -> 2
            else -> enchant.maxLevel
        }
        if (maxLevel < level + 1) return fail(event,"このアイテムのエンチャントは最大レベルを超えるため出来ません!")

        item.enchant(enchantSet,level + 1)
        event.player.playSound(event.player, Sound.BLOCK_ENCHANTMENT_TABLE_USE,1f,1f)

        event.isCancelled = true
        event.bukkitEvent.view.setCursor(ItemStack.empty())
    }

    fun fail(event: ItemEvents.SwapItemInInventoryEvent,message: String? = null) {
        if (message != null) {
            event.player.sendMessage(Component.text(message).color(NamedTextColor.RED))
        }

        event.bukkitEvent.isCancelled = true
        event.player.playSound(event.player, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED,1f,0f)
    }
}