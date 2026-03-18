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
    val MAX_ENCHANTMENT_LEVLES = mapOf(
        Enchantment.PROTECTION to 2,
        Enchantment.SHARPNESS to 3,
        Enchantment.DENSITY to 2,
        Enchantment.BREACH to 2,
        Enchantment.LUNGE to 2,
        Enchantment.POWER to 3,
    )

    @EventHandler
    fun onPrepareEnchant(event: PrepareItemEnchantEvent) {
        event.offers.forEach { offer ->
            if (offer == null) return@forEach

            val maxLevel = MAX_ENCHANTMENT_LEVLES[offer.enchantment]
            if (maxLevel != null && maxLevel < offer.enchantmentLevel) {
                offer.enchantmentLevel = maxLevel
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
            val maxLevel = MAX_ENCHANTMENT_LEVLES[enchant]
            if (maxLevel != null && maxLevel < level) {
                modifies[enchant] = maxLevel
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
            val maxLevel = MAX_ENCHANTMENT_LEVLES[enchant]
            if (maxLevel != null && maxLevel < level) {
                if (level > maxLevel) {
                    item.addUnsafeEnchantment(enchant, maxLevel)
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