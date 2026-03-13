package me.kaitp1016.ffa.items

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

enum class Rarity(val color: TextColor,val rarityName: String) {
    COMMON(color = NamedTextColor.WHITE, rarityName = "Common",),
    UNCOMMON(color = NamedTextColor.GREEN, rarityName = "Uncommon",),
    RARE(color = NamedTextColor.BLUE, rarityName = "Rare",),
    EPIC(color = NamedTextColor.DARK_PURPLE, rarityName = "Epic",),
    LEGENDARY(color = NamedTextColor.GOLD, rarityName = "Legendary",),
    MYTHIC(color = NamedTextColor.LIGHT_PURPLE, rarityName = "Mythic",),
    ADMIN(color = NamedTextColor.DARK_RED, rarityName = "Admin",),
}