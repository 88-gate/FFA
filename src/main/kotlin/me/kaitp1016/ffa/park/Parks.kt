package me.kaitp1016.ffa.park

import me.kaitp1016.ffa.park.impl.AbsorptionPark
import me.kaitp1016.ffa.park.impl.ArcherPark
import me.kaitp1016.ffa.park.impl.ArrowRefillPark
import me.kaitp1016.ffa.park.impl.AutoFeedPark
import me.kaitp1016.ffa.park.impl.DeathAdvantagePark
import me.kaitp1016.ffa.park.impl.DefensePark
import me.kaitp1016.ffa.park.impl.EarlyAdvantagePark
import me.kaitp1016.ffa.park.impl.EmptyPark
import me.kaitp1016.ffa.park.impl.ExperiencePark
import me.kaitp1016.ffa.park.impl.FirstStrikePark
import me.kaitp1016.ffa.park.impl.HealthBoostPark
import me.kaitp1016.ffa.park.impl.JumpBoostPark
import me.kaitp1016.ffa.park.impl.LastStandPark
import me.kaitp1016.ffa.park.impl.MidasPark
import me.kaitp1016.ffa.park.impl.MoreKnockbackPark
import me.kaitp1016.ffa.park.impl.PyroPark
import me.kaitp1016.ffa.park.impl.ResistancePark
import me.kaitp1016.ffa.park.impl.RunnerPark
import me.kaitp1016.ffa.park.impl.SlownessPark
import me.kaitp1016.ffa.park.impl.SpeedPark
import me.kaitp1016.ffa.park.impl.ThornPark
import me.kaitp1016.ffa.park.impl.WarriorPark

object Parks {
    private val ALL_PARKS = mutableListOf<Park>()
    private val PARK_MAP = mutableMapOf<String, Park>()
    private val PARK_IDS = mutableMapOf<Park, String>()

    val EMPTY = register("empty", EmptyPark())
    val ARCHER = register("archer", ArcherPark())
    val WARRIOR = register("warrior", WarriorPark())
    val EXPERIENCE = register("experience", ExperiencePark())
    val HEALTH_BOOST = register("health_boost", HealthBoostPark())
    val MORE_KNOCKBACK = register("more_knockback", MoreKnockbackPark())
    val SPEED = register("speed", SpeedPark())
    val ABSORPTION = register("absorption", AbsorptionPark())
    val ARROW_REFILL = register("arrow_refill", ArrowRefillPark())
    val MIDAS = register("midas", MidasPark())
    val RESISTANCE = register("resistance", ResistancePark())
    val DEFENSE = register("defense", DefensePark())
    val THORN = register("thorn", ThornPark())
    val JUMP_BOOST = register("jump_boost", JumpBoostPark())
    val PYRO = register("pyro", PyroPark())
    val SLOWNESS = register("slowness", SlownessPark())
    val RUNNER = register("runner", RunnerPark())
    val AUTO_FEED = register("auto_feed", AutoFeedPark())
    val LAST_STAND = register("last_stand", LastStandPark())
    val DEATH_ADVANTAGE = register("death_advantage", DeathAdvantagePark())
    val EARLY_ADVANTAGE = register("early_advantage", EarlyAdvantagePark())
    val FIRST_STRIKE = register("first_strike", FirstStrikePark())

    fun <T: Park> register(id: String, park: T): T {
        ALL_PARKS.add(park)
        PARK_MAP[id] = park
        PARK_IDS[park] = id
        park.register()

        return park
    }

    fun getPark(id: String): Park {
        return PARK_MAP[id]!!
    }

    fun getId(park: Park): String {
        return PARK_IDS[park]!!
    }

    fun entries(): List<Park> {
        return ALL_PARKS
    }
}