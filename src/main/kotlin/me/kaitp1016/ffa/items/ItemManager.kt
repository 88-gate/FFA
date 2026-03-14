package me.kaitp1016.ffa.items

import me.kaitp1016.ffa.items.events.ItemEventManager
import me.kaitp1016.ffa.items.impl.admin.*
import me.kaitp1016.ffa.items.impl.consumeable.*
import me.kaitp1016.ffa.items.impl.misc.*
import me.kaitp1016.ffa.items.impl.tool.DiamondShear
import me.kaitp1016.ffa.items.impl.tool.DiamondSmeltPickaxe
import me.kaitp1016.ffa.items.impl.tool.FlintShovel
import me.kaitp1016.ffa.items.impl.tool.SmeltPickaxe
import me.kaitp1016.ffa.items.impl.weapon.*
import me.kaitp1016.ffa.plugin
import org.bukkit.NamespacedKey
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemManager {
    val NAMESPACED_KEY_ITEM_ID = NamespacedKey(plugin,"item_id")

    val items = arrayOf(AdminSword, SuperSword, AdminArmor.AdminBoots, AdminArmor.AdminLeggings, AdminArmor.AdminChestplate, AdminArmor.AdminHelmet,AspectOfTheVoid, SmeltPickaxe, DiamondSmeltPickaxe, RefinedDiamondSword, AwakenedDiamondSword, PortableAnvil, PortableCraftingTable, PortableEnchantingTable, MegaLongBow, GoldenHead, DiamondShear, InstantWall, RandomCookie,CookieClickerCookie, FastSword, InstantProtFourArmor, InstantProtEightArmor, FFAKitSelector, FlyFeather, GrapplingHook,GrapplingBow, Keraunos, Pratidhvani, OdinSpear, Excalibur, BeamSword, FlintSword, FlintShovel, Fireball,ThrowableTNT, Overenchant, Caladbolg,SettingBook,EmeraldBlade, Durandal, OldSword, Berserker, HealingSword)
    val itemIdMap = mutableMapOf<String, CustomItem>()

    private var nextInternalId = 0

    fun registeAll() {
        items.forEach(::register)
    }

    fun register(item: CustomItem) {
        this.itemIdMap[item.id] = item
        ItemEventManager.register(item)

        if (item is Listener) {
            plugin.server.pluginManager.registerEvents(item,plugin)
        }
    }

    fun getInternalId(): Int {
        return ++nextInternalId
    }

    fun ItemStack.getBattleRoyalItemID(): String? {
        return this.persistentDataContainer.get(NAMESPACED_KEY_ITEM_ID, PersistentDataType.STRING)
    }

    fun ItemStack.getBattleRoyalItem(): CustomItem? {
        return itemIdMap[this.persistentDataContainer.get(NAMESPACED_KEY_ITEM_ID, PersistentDataType.STRING)]
    }

    fun ItemStack.isBattleRoyalItem(): Boolean {
        return this.persistentDataContainer.has(NAMESPACED_KEY_ITEM_ID, PersistentDataType.STRING)
    }
}