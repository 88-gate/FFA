package me.kaitp1016.ffa.perk.extra

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.kaitp1016.ffa.gson
import me.kaitp1016.ffa.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import java.io.File
import java.util.UUID

object ExtraPerkManager: Listener {
    private val EXTRA_PERK_DATA_FILE = File(plugin.dataFolder, "extra_perks.json").also { it.parentFile.mkdirs() }

    data class PlayerExtraPerkData(val uuid: UUID, var unlockedSlots: Int, val unlockedPerks: MutableList<ExtraPerk>, val selectedPerks: MutableList<ExtraPerk>) {
        companion object {
            fun default(player: UUID): PlayerExtraPerkData {
                return PlayerExtraPerkData(player, 0, mutableListOf(ExtraPerks.EMPTY), mutableListOf())
            }
        }
    }

    private val playerPerks = load()

    fun getPerkData(uuid: UUID): PlayerExtraPerkData {
        return playerPerks.getOrPut(uuid) { PlayerExtraPerkData.default(uuid) }
    }

    fun getUnlockCost(index: Int): Int {
        return index * index * 25000
    }

    @EventHandler
    fun onDisablePlugin(event: PluginDisableEvent) {
        if (plugin == event.plugin) {
            save()
        }
    }

    private fun load(): MutableMap<UUID, PlayerExtraPerkData> {
        if (!EXTRA_PERK_DATA_FILE.exists()) return mutableMapOf()

        val json = gson.fromJson(EXTRA_PERK_DATA_FILE.readText(), JsonObject::class.java)
        val players = mutableMapOf<UUID, PlayerExtraPerkData>()

        json.keySet().forEach { key ->
            val data = json.get(key).asJsonObject
            val uuid = UUID.fromString(data.get("uuid").asString)
            val unlockedSlots = data.get("unlocked_slots").asInt
            val unlockedPerks = data.get("unlocked_parks").asJsonArray.map { ExtraPerks.getPerk(it.asString) }
            val selectedPerks = data.get("selected_parks").asJsonArray.map { ExtraPerks.getPerk(it.asString) }

            players[uuid] = PlayerExtraPerkData(uuid, unlockedSlots, unlockedPerks.toMutableList(), selectedPerks.toMutableList())
        }

        return players
    }

    private fun save() {
        if (!EXTRA_PERK_DATA_FILE.exists()) {
            EXTRA_PERK_DATA_FILE.parentFile.mkdirs()
            EXTRA_PERK_DATA_FILE.createNewFile()
        }

        val json = JsonObject()

        playerPerks.forEach { (uuid, player) ->
            val perk = JsonObject()
            perk.addProperty("uuid", player.uuid.toString())
            perk.addProperty("unlocked_slots", player.unlockedSlots)
            perk.add("unlocked_parks", JsonArray().also { array -> player.unlockedPerks.forEach { array.add(ExtraPerks.getId(it)) } })
            perk.add("selected_parks", JsonArray().also { array -> player.selectedPerks.forEach { array.add(ExtraPerks.getId(it)) } })

            json.add(uuid.toString(), perk  )
        }

        EXTRA_PERK_DATA_FILE.writeText(gson.toJson(json))
    }
}