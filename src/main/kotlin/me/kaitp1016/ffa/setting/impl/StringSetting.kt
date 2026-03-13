package me.kaitp1016.ffa.setting.impl

import me.kaitp1016.ffa.setting.Setting
import me.kaitp1016.ffa.setting.SettingManager

class StringSetting: Setting<String> {
    override val description: String
    override val name: String
    override val default: String

    private var field: String

    constructor(name:String, default: String, description: String = "") {
        this.name = name
        this.description = description
        this.default = default
        this.field = SettingManager.get(name)?.asString ?: default

        SettingManager.settings.add(this)
    }

    override fun getValue(): String {
        return field
    }

    override fun setValue(value: String) {
        field = value
        SettingManager.set(name,value)
    }

    override fun parse(text: String): String {
        return text
    }
}