package me.kaitp1016.ffa.perk.extra

import me.kaitp1016.ffa.perk.extra.impl.*

object ExtraPerks {
    private val ALL_PERKS = mutableListOf<ExtraPerk>()
    private val PERK_MAP = mutableMapOf<String, ExtraPerk>()
    private val PERK_IDS = mutableMapOf<ExtraPerk, String>()

    val EMPTY = register("empty", EmptyPerk())
    val BLOOD_BERSERKER = register("blood_berserker", BloodBerserkerPerk())
    val RANGED_LIFE_STEALER = register("ramned_life_stealer", RangedLifeStealerPerk())
    val STREAK_LOVER = register("streak_lover", StreakLoverPerk())
    val TOUGHNESS = register("toughness", ToughnessPerk())
    val BOUNTIFUL_STREAK_PERK = register("bountiful_streak", BountifulStreakPerk())

    fun <T: ExtraPerk> register(id: String, perk: T): T {
        ALL_PERKS.add(perk)
        PERK_MAP[id] = perk
        PERK_IDS[perk] = id
        perk.register()

        return perk
    }

    fun getPerk(id: String): ExtraPerk {
        return PERK_MAP[id]!!
    }

    fun getId(perk: ExtraPerk): String {
        return PERK_IDS[perk]!!
    }

    fun entries(): List<ExtraPerk> {
        return ALL_PERKS
    }
}