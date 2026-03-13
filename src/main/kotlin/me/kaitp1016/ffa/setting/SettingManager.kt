package me.kaitp1016.ffa.setting

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.kaitp1016.ffa.gson
import me.kaitp1016.ffa.plugin
import java.io.File

object SettingManager {
    val file = File(plugin.dataFolder, "setting.json")

    private val json = if (!file.exists()) {
        JsonObject()
    } else {
        gson.fromJson(file.readText(), JsonObject::class.java)
    }

    val settings: MutableList<Setting<*>> = mutableListOf<Setting<*>>()

    fun get(name: String): JsonElement? {
        return json.get(name)
    }

    fun set(name: String,value: JsonElement) {
        json.add(name,value)
        save()
    }

    fun set(name:String,value: Number) {
        json.addProperty(name,value)
        save()
    }

    fun set(name:String,value: String) {
        json.addProperty(name,value)
        save()
    }

    fun set(name:String,value: Boolean) {
        json.addProperty(name,value)
        save()
    }

    fun save() {
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        file.writeText(gson.toJson(json))
    }
}