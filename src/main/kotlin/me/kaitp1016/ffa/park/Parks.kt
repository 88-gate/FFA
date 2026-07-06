package me.kaitp1016.ffa.park

import me.kaitp1016.ffa.park.impl.ArcherPark
import me.kaitp1016.ffa.park.impl.EmptyPark
import me.kaitp1016.ffa.park.impl.ExperiencePark
import me.kaitp1016.ffa.park.impl.WarriorPark

object Parks {
    private val ALL_PARKS = mutableListOf<Park>()
    private val PARK_MAP = mutableMapOf<String, Park>()
    private val PARK_IDS = mutableMapOf<Park, String>()

    val EMPTY = register("empty", EmptyPark())
    val ARCHER = register("archer", ArcherPark())
    val WARRIOR = register("warrior", WarriorPark())
    val EXPERIENCE = register("experience", ExperiencePark())

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