package me.kaitp1016.ffa.setting.impl

import me.kaitp1016.ffa.setting.Setting
import me.kaitp1016.ffa.setting.SettingManager

class BooleanSetting: Setting<Boolean> {
    override val description: String
    override val name: String
    override val default: Boolean

    private var field: Boolean

    constructor(name:String, default: Boolean = false, description: String = "") {
        this.name = name
        this.description = description
        this.default = default
        this.field = SettingManager.get(name)?.asBoolean ?: default

        SettingManager.settings.add(this)
    }

    override fun getValue(): Boolean {
        return field
    }

    override fun setValue(value: Boolean) {
        field = value
        SettingManager.set(name,value)
    }

    override fun parse(text: String): Boolean? {
        return when (text.lowercase()) {
            "true","yes" -> true
            "false","no" -> false
            else -> null
        }
    }
}