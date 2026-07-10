package me.kaitp1016.ffa.perk

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.kaitp1016.ffa.gson
import me.kaitp1016.ffa.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import java.io.File
import java.util.UUID

object PerkManager: Listener {
    private val PERK_DATA_FILE = File(plugin.dataFolder, "parks.json").also { it.parentFile.mkdirs() }

    data class PlayerPerkData(val uuid: UUID, var unlockedSlots: Int, val unlockedPerks: MutableList<Perk>, val selectedPerks: MutableList<Perk>) {
        companion object {
            fun default(player: UUID): PlayerPerkData {
                return PlayerPerkData(player, 0, mutableListOf(Perks.EMPTY), mutableListOf())
            }
        }
    }

    private val playerPerks = load()

    fun getPerkData(uuid: UUID): PlayerPerkData {
        return playerPerks.getOrPut(uuid) { PlayerPerkData.default(uuid) }
    }

    fun getUnlockCost(index: Int): Int {
        return index * index * 2000
    }

    @EventHandler
    fun onDisablePlugin(event: PluginDisableEvent) {
        if (plugin == event.plugin) {
            save()
        }
    }

    private fun load(): MutableMap<UUID, PlayerPerkData> {
        if (!PERK_DATA_FILE.exists()) return mutableMapOf()

        val json = gson.fromJson(PERK_DATA_FILE.readText(), JsonObject::class.java)
        val players = mutableMapOf<UUID, PlayerPerkData>()

        json.keySet().forEach { key ->
            val data = json.get(key).asJsonObject
            val uuid = UUID.fromString(data.get("uuid").asString)
            val unlockedSlots = data.get("unlocked_slots").asInt
            val unlockedPerks = data.get("unlocked_parks").asJsonArray.map { Perks.getPerk(it.asString) }
            val selectedPerks = data.get("selected_parks").asJsonArray.map { Perks.getPerk(it.asString) }

            players[uuid] = PlayerPerkData(uuid, unlockedSlots, unlockedPerks.toMutableList(), selectedPerks.toMutableList())
        }

        return players
    }

    private fun save() {
        if (!PERK_DATA_FILE.exists()) {
            PERK_DATA_FILE.parentFile.mkdirs()
            PERK_DATA_FILE.createNewFile()
        }

        val json = JsonObject()

        playerPerks.forEach { (uuid, player) ->
            val perk = JsonObject()
            perk.addProperty("uuid", player.uuid.toString())
            perk.addProperty("unlocked_slots", player.unlockedSlots)
            perk.add("unlocked_parks", JsonArray().also { array -> player.unlockedPerks.forEach { array.add(Perks.getId(it)) } })
            perk.add("selected_parks", JsonArray().also { array -> player.selectedPerks.forEach { array.add(Perks.getId(it)) } })

            json.add(uuid.toString(), perk  )
        }

        PERK_DATA_FILE.writeText(gson.toJson(json))
    }
}