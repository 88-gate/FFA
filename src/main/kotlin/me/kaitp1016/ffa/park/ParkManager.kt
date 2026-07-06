package me.kaitp1016.ffa.park

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.kaitp1016.ffa.gson
import me.kaitp1016.ffa.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import java.io.File
import java.util.UUID

object ParkManager: Listener {
    private val PARK_DATA_FILE = File(plugin.dataFolder, "parks.json").also { it.parentFile.mkdirs() }

    data class PlayerPark(val uuid: UUID, var unlockedSlots: Int, val unlockedParks: MutableList<Park>, val selectedParks: MutableList<Park>) {
        companion object {
            fun default(player: UUID): PlayerPark {
                return PlayerPark(player,0,mutableListOf(Parks.EMPTY),mutableListOf())
            }
        }
    }

    private val playerParks = load()

    fun getPark(uuid: UUID): PlayerPark {
        return playerParks.getOrPut(uuid) { PlayerPark.default(uuid) }
    }

    fun getUnlockCost(index: Int): Int {
        return index * index * 1000
    }

    @EventHandler
    fun onDisablePlugin(event: PluginDisableEvent) {
        if (plugin == event.plugin) {
            save()
        }
    }

    private fun load(): MutableMap<UUID, PlayerPark> {
        if (!PARK_DATA_FILE.exists()) return mutableMapOf()

        val json = gson.fromJson(PARK_DATA_FILE.readText(), JsonObject::class.java)
        val players = mutableMapOf<UUID, PlayerPark>()

        json.keySet().forEach { key ->
            val data = json.get(key).asJsonObject
            val uuid = UUID.fromString(data.get("uuid").asString)
            val unlockedSlots = data.get("unlocked_slots").asInt
            val unlockedParks = data.get("unlocked_parks").asJsonArray.map { Parks.getPark(it.asString) }
            val selectedParks = data.get("selected_parks").asJsonArray.map { Parks.getPark(it.asString) }

            players[uuid] = PlayerPark(uuid, unlockedSlots, unlockedParks.toMutableList(), selectedParks.toMutableList())
        }

        return players
    }

    private fun save() {
        if (!PARK_DATA_FILE.exists()) {
            PARK_DATA_FILE.parentFile.mkdirs()
            PARK_DATA_FILE.createNewFile()
        }

        val json = JsonObject()

        playerParks.forEach { (uuid, player) ->
            val park = JsonObject()
            park.addProperty("uuid",player.uuid.toString())
            park.addProperty("unlocked_slots",player.unlockedSlots)
            park.add("unlocked_parks", JsonArray().also { array -> player.unlockedParks.forEach { array.add(Parks.getId(it)) }})
            park.add("selected_parks", JsonArray().also { array -> player.selectedParks.forEach { array.add(Parks.getId(it)) }})

            json.add(uuid.toString(), park)
        }

        PARK_DATA_FILE.writeText(gson.toJson(json))
    }
}