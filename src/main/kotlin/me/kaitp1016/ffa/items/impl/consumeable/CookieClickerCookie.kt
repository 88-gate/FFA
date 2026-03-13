package me.kaitp1016.ffa.items.impl.consumeable

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.ItemCategory
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.events.ItemEvents
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftItemStack
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.item.component.ItemLore
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapelessRecipe
import kotlin.jvm.optionals.getOrNull

object CookieClickerCookie: CustomItem() {
    override val id = "COOKIE_CLICKER_COOKIE"
    override val name = "クッキークリッカーのクッキー"
    override val description = "インベントリで右クリックをするとクッキーが増える"
    override val material = Material.COOKIE
    override val rarity = Rarity.RARE
    override val category = ItemCategory.CONSUMEABLE

    override fun createItem(amount: Int): ItemStack {
        return super.createItem(amount).asCraftItemStack().handle.apply {
            val originalData = this.get(DataComponents.CUSTOM_DATA)?.copyTag() ?: CompoundTag()

            this.set(DataComponents.CUSTOM_DATA, CustomData.of(originalData.apply {
                this.putInt("cookies",1)
            }))
            this.set(DataComponents.MAX_STACK_SIZE,1)
        }.bukkitStack
    }

    override fun getLore(item: ItemStack?): List<Component> {
        val cookies = if (item == null) 1 else getCookie(item)

        return super.getLore(item).toMutableList().apply {
            this.addFirst(Component.empty())
            this.addFirst(Component.text("${cookies}クッキー").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC,false))
        }
    }

    override fun getRecipes(): List<Recipe>? {
        return listOf(
            ShapelessRecipe(NamespacedKey(plugin, "cookie_clicker_cookie"), this.createItem()).apply {
                this.addIngredient(Material.COOKIE)
            }
        )
    }

    fun getCookie(item: ItemStack): Int {
        return item.asCraftItemStack().handle.get(DataComponents.CUSTOM_DATA)?.copyTag()?.getInt("cookies")?.getOrNull() ?: 1
    }

    fun setCookie(item: ItemStack, cookie: Int): ItemStack {
        return item.asCraftItemStack().handle.apply {
            val originalData = this.get(DataComponents.CUSTOM_DATA)!!.copyTag()

            this.set(DataComponents.CUSTOM_DATA, CustomData.of(originalData.apply {
                this.putInt("cookies",cookie)
            }))

            this.set(DataComponents.LORE, ItemLore(getLore(item).map { it.toMCComponent() }))
        }.bukkitStack
    }

    @ItemEventHandler
    fun onConsume(event: ItemEvents.ConsumeEvent) {
        val item = event.item

        val cookies = getCookie(event.item)
        if (cookies > 1) {
            event.bukkitEvent.replacement = setCookie(item,cookies - 1)
        }
    }

    @ItemEventHandler
    fun onClick(event: ItemEvents.ClickItemInInventoryEvent) {
        if (event.bukkitEvent.click != ClickType.RIGHT) return

        val item = event.item
        val cookies = getCookie(event.item)

        setCookie(item, cookies + 1)

        event.player.playSound(event.player, Sound.BLOCK_SNIFFER_EGG_PLOP,1f,2f)
        event.isCancelled = true
    }
}