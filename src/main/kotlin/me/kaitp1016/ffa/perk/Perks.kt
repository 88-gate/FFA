package me.kaitp1016.ffa.perk

import me.kaitp1016.ffa.perk.impl.*

object Perks {
    private val ALL_PERKS = mutableListOf<Perk>()
    private val PERK_MAP = mutableMapOf<String, Perk>()
    private val PERK_IDS = mutableMapOf<Perk, String>()

    val EMPTY = register("empty", EmptyPerk())
    val ARCHER = register("archer", ArcherPerk())
    val WARRIOR = register("warrior", WarriorPerk())
    val EXPERIENCE = register("experience", ExperiencePerk())
    val HEALTH_BOOST = register("health_boost", HealthBoostPerk())
    val MORE_KNOCKBACK = register("more_knockback", MoreKnockbackPerk())
    val SPEED = register("speed", SpeedPerk())
    val ABSORPTION = register("absorption", AbsorptionPerk())
    val ARROW_REFILL = register("arrow_refill", ArrowRefillPerk())
    val MIDAS = register("midas", MidasPerk())
    val RESISTANCE = register("resistance", ResistancePerk())
    val DEFENSE = register("defense", DefensePerk())
    val THORN = register("thorn", ThornPerk())
    val JUMP_BOOST = register("jump_boost", JumpBoostPerk())
    val PYRO = register("pyro", PyroPerk())
    val SLOWNESS = register("slowness", SlownessPerk())
    val RUNNER = register("runner", RunnerPerk())
    val AUTO_FEED = register("auto_feed", AutoFeedPerk())
    val LAST_STAND = register("last_stand", LastStandPerk())
    val DEATH_ADVANTAGE = register("death_advantage", DeathAdvantagePerk())
    val EARLY_ADVANTAGE = register("early_advantage", EarlyAdvantagePerk())
    val FIRST_STRIKE = register("first_strike", FirstStrikePerk())
    val QUADRUPLE_ARROW = register("quadruple_arrow", QuadrupleArrowPerk())

    fun <T: Perk> register(id: String, perk: T): T {
        ALL_PERKS.add(perk)
        PERK_MAP[id] = perk
        PERK_IDS[perk] = id
        perk.register()

        return perk
    }

    fun getPerk(id: String): Perk {
        return PERK_MAP[id]!!
    }

    fun getId(perk: Perk): String {
        return PERK_IDS[perk]!!
    }

    fun entries(): List<Perk> {
        return ALL_PERKS
    }
}