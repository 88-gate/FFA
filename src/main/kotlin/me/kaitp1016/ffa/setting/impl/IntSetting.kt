package me.kaitp1016.ffa.setting.impl

import me.kaitp1016.ffa.setting.Setting
import me.kaitp1016.ffa.setting.SettingManager

class IntSetting: Setting<Int> {
    override val description: String
    override val name: String
    override val default: Int

    val min: Int
    val max:Int

    private var field: Int

    constructor(name:String,default:Int = 0,min:Int = Int.MIN_VALUE,max:Int = Int.MAX_VALUE,description: String = "") {
        this.name = name
        this.description = description
        this.default = default
        this.min = min
        this.max = max
        this.field = SettingManager.get(name)?.asInt ?: default

        SettingManager.settings.add(this)
    }

    override fun getValue(): Int {
        return field
    }

    override fun setValue(value: Int) {
        if (value > max || value < min) throw IllegalArgumentException("範囲外です! ($min-$max の間である必要があります)")

        field = value
        SettingManager.set(name,value)
    }

    override fun parse(text: String): Int? {
        return text.toIntOrNull()
    }
}