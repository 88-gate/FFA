package me.kaitp1016.ffa.items

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

enum class Rarity(val color: TextColor, val colorCode: String, val rarityName: String) {
    COMMON(color = NamedTextColor.WHITE,colorCode = "§f", rarityName = "Common",),
    UNCOMMON(color = NamedTextColor.GREEN,colorCode = "§f", rarityName = "Uncommon",),
    RARE(color = NamedTextColor.BLUE,colorCode = "§9", rarityName = "Rare",),
    EPIC(color = NamedTextColor.DARK_PURPLE,colorCode = "§5", rarityName = "Epic",),
    LEGENDARY(color = NamedTextColor.GOLD,colorCode = "§6", rarityName = "Legendary",),
    MYTHIC(color = NamedTextColor.LIGHT_PURPLE,colorCode = "§d", rarityName = "Mythic",),
    ADMIN(color = NamedTextColor.DARK_RED,colorCode = "§c", rarityName = "Admin",),
}