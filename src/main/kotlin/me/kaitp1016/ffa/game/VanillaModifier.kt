package me.kaitp1016.ffa.game

import me.kaitp1016.ffa.items.ItemManager.getBattleRoyalItem
import me.kaitp1016.ffa.items.ItemManager.isBattleRoyalItem
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.enchantment.PrepareItemEnchantEvent
import org.bukkit.event.inventory.PrepareAnvilEvent

object VanillaModifier: Listener {
    const val MAX_PROTECTION_LEVEL = 2

    @EventHandler
    fun onPrepareEnchant(event: PrepareItemEnchantEvent) {
        event.offers.forEach { offer ->
            if (offer == null) return@forEach

            when (offer.enchantment) {
                Enchantment.PROTECTION -> {
                    if (offer.enchantmentLevel > MAX_PROTECTION_LEVEL) offer.enchantmentLevel = MAX_PROTECTION_LEVEL
                }
            }
        }

        val item = event.item

        if (item.isBattleRoyalItem()) {
            val battleRoyalItem = item.getBattleRoyalItem()!!

            if (!battleRoyalItem.isEnchantable) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onEnchant(event: EnchantItemEvent) {
        val modifies = mutableMapOf<Enchantment, Int?>()

        event.enchantsToAdd.forEach { (enchant, level) ->
            when (enchant) {
                Enchantment.PROTECTION -> {
                    if (level > MAX_PROTECTION_LEVEL) modifies[Enchantment.PROTECTION] = MAX_PROTECTION_LEVEL
                }
            }
        }

        modifies.forEach { (enchant, level) ->
            if (level == null) {
                event.enchantsToAdd.remove(enchant)
            } else {
                event.enchantsToAdd[enchant] = level
            }
        }
    }

    @EventHandler
    fun onPrepareAnvil(event: PrepareAnvilEvent) {
        val item = event.result ?: return

        item.enchantments.forEach { (enchant, level) ->
            when (enchant) {
                Enchantment.PROTECTION -> {
                    if (level > 2) item.addUnsafeEnchantment(Enchantment.PROTECTION, 2)
                }
            }
        }

        if (item.isBattleRoyalItem()) {
            val battleRoyalItem = item.getBattleRoyalItem()!!

            if (!battleRoyalItem.isEnchantable) {
                event.view.repairCost = Int.MAX_VALUE
            }
        }
    }
}