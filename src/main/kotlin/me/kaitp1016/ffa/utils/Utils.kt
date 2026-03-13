package me.kaitp1016.ffa.utils

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.ceil

object Utils {
    fun filterAndSortMostMatch(collection: Collection<String?>, value: String?): List<String> {
        if (value == null) return collection.filterNotNull()

        val mutableList = collection.filterNotNull().toMutableList()

        mutableList.removeAll {
            return@removeAll !it.lowercase().contains(value.lowercase())
        }

        mutableList.sortBy {
            if (it.lowercase().startsWith(value.lowercase())) 1
            else if (it.lowercase().contains(value.lowercase())) 2
            else 0
        }

        return mutableList
    }

    fun Player.consumeItem(predicate:(ItemStack) -> Boolean,amount:Int = 1): Boolean {
        val inventory = this.inventory

        val mainHand = inventory.itemInMainHand
        if (mainHand.amount >= amount && predicate(mainHand)) {
            mainHand.amount -= amount
            return true
        }

        val offHand = inventory.itemInOffHand
        if (offHand.amount >= amount && predicate(offHand)) {
            offHand.amount -= amount
            return true
        }

        inventory.forEach { item ->
            if (item != null && item.amount >= amount && predicate(item)) {
                item.amount -= amount
                return true
            }
        }

        return false
    }

    fun <T>T?.equalsOneOf(vararg args: Any): Boolean {
        return args.any { it.equals(this@equalsOneOf) }
    }
}