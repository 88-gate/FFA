package me.kaitp1016.ffa.items

import me.kaitp1016.ffa.items.ItemManager.getBattleRoyalItemID
import me.kaitp1016.ffa.items.ItemManager.isBattleRoyalItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.persistence.PersistentDataType

abstract class CustomItem {
    abstract val id: String
    abstract val name: String
    abstract val rarity: Rarity
    abstract val material: Material

    open val category: ItemCategory? = null
    open val description: String? = null
    open val history: String? = null
    open val additionalInfo: List<Component>? = null

    open val isUnique = false
    open val isEnchantable = true

    var internalId = ItemManager.getInternalId()

    open fun createItem(amount: Int = 1): ItemStack {
        return ItemStack(material).apply {
            val lore = getLore(this)
            this.lore(lore)
            this.amount = amount

            this.editMeta {
                it.itemName(getDisplayName(this))
                it.persistentDataContainer.set(ItemManager.NAMESPACED_KEY_ITEM_ID, PersistentDataType.STRING,id)
            }
        }
    }

    open fun getDisplayName(item: ItemStack? = null): Component {
        return Component.text(name).color(rarity.color).decoration(TextDecoration.ITALIC,false)
    }

    open fun getLore(item: ItemStack?): List<Component> {
        val lore = mutableListOf<Component>()

        if (history != null) {
            history!!.split("\n").forEach {
                lore.add(Component.text(it).style(Style.style().color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC,true).build()))
            }
            lore.add(Component.empty())
        }

        if (description != null) {
            description!!.split("\n").forEach {
                lore.add(Component.text(it).style(Style.style().color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC,false).build()))
            }
            lore.add(Component.empty())
        }

        if (additionalInfo != null) {
            additionalInfo!!.forEach {
                if (it.hasDecoration(TextDecoration.ITALIC)) {
                    lore.add(it)
                }
                else {
                    lore.add(it.decoration(TextDecoration.ITALIC,false))
                }
            }
            lore.add(Component.empty())
        }

        if (category != null)  {
            if (category == ItemCategory.MISC) {
                lore.add(Component.text(rarity.rarityName.uppercase()).style(Style.style().color(rarity.color).decoration(TextDecoration.ITALIC,false).decoration(TextDecoration.BOLD,true).build()))
            }
            else {
                lore.add(Component.text("${rarity.rarityName.uppercase()} ${category!!.categoryName.uppercase()}").style(Style.style().color(rarity.color).decoration(TextDecoration.ITALIC,false).decoration(TextDecoration.BOLD,true).build()))
            }
        }

        return lore
    }

    open fun getRecipes(): List<Recipe>? {
        return null
    }

    open fun consumeOrMessage(player: Player,amount:Int = 1,message: Component = Component.text("アイテムが見つかりませんでした。")): Boolean {
        val inventory = player.inventory

        val mainHand = inventory.itemInMainHand
        if (mainHand.isBattleRoyalItem() && mainHand.getBattleRoyalItemID() == this.id && mainHand.amount >= amount) {
            mainHand.amount--
            return true
        }

        val offHand = inventory.itemInOffHand
        if (offHand.isBattleRoyalItem() && offHand.getBattleRoyalItemID() == this.id && offHand.amount >= amount) {
            offHand.amount--
            return true
        }

        inventory.forEach { item ->
            if (item.isBattleRoyalItem() && item.getBattleRoyalItemID() == this.id && item.amount >= amount) {
                item.amount--
                return true
            }
        }

        player.sendMessage(message)
        return false
    }
}