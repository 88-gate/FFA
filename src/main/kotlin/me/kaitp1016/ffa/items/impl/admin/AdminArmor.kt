package me.kaitp1016.ffa.items.impl.admin

import me.kaitp1016.ffa.items.CustomItem
import me.kaitp1016.ffa.items.Rarity
import me.kaitp1016.ffa.items.events.ArmorEvents
import me.kaitp1016.ffa.items.events.ItemEventHandler
import me.kaitp1016.ffa.items.ItemCategory
import org.bukkit.Material

object AdminArmor {
    object AdminBoots: CustomItem() {
        override val id = "ADMIN_BOOTS"
        override val name = "Admin Boots"
        override val rarity = Rarity.ADMIN
        override val material = Material.DIAMOND_BOOTS
        override val category = ItemCategory.ADMIN

        @ItemEventHandler
        fun onDamage(event: ArmorEvents.DamageEvent) {
            event.bukkitEvent.damage = 0.0
        }
    }

    object AdminLeggings: CustomItem() {
        override val id = "ADMIN_LEGGINGS"
        override val name = "Admin Leggings"
        override val rarity = Rarity.ADMIN
        override val material = Material.DIAMOND_LEGGINGS

        @ItemEventHandler
        fun onDamage(event: ArmorEvents.DamageEvent) {
            event.bukkitEvent.damage = 0.0
        }
    }

    object AdminChestplate: CustomItem() {
        override val id = "ADMIN_CHESTPLATE"
        override val name = "Admin Chestplate"
        override val rarity = Rarity.ADMIN
        override val material = Material.DIAMOND_CHESTPLATE

        @ItemEventHandler
        fun onDamage(event: ArmorEvents.DamageEvent) {
            event.bukkitEvent.damage = 0.0
        }
    }

    object AdminHelmet: CustomItem() {
        override val id = "ADMIN_HELMET"
        override val name = "Admin Helmet"
        override val rarity = Rarity.ADMIN
        override val material = Material.DIAMOND_HELMET

        @ItemEventHandler
        fun onDamage(event: ArmorEvents.DamageEvent) {
            event.bukkitEvent.damage = 0.0
        }
    }
}
