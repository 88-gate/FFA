package me.kaitp1016.ffa.setting.impl

import me.kaitp1016.ffa.setting.Setting
import me.kaitp1016.ffa.setting.SettingManager

class DoubleSetting: Setting<Double> {
    override val description: String
    override val name: String
    override val default: Double

    val min: Double
    val max: Double

    private var field: Double

    constructor(name:String,default:Double = 0.0,min:Double = Double.MIN_VALUE,max:Double = Double.MAX_VALUE,description: String = "") {
        this.name = name
        this.description = description
        this.default = default
        this.min = min
        this.max = max
        this.field = SettingManager.get(name)?.asDouble ?: default

        SettingManager.settings.add(this)
    }

    override fun getValue(): Double {
        return field
    }

    override fun setValue(value: Double) {
        if (value > max || value < min) throw IllegalArgumentException("範囲外です! ($min-$max の間である必要があります)")

        field = value
        SettingManager.set(name,value)
    }

    override fun parse(text: String): Double? {
        return text.toDoubleOrNull()
    }
}