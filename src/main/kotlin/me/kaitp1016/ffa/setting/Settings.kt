package me.kaitp1016.ffa.setting

import me.kaitp1016.ffa.setting.impl.DoubleSetting
import me.kaitp1016.ffa.setting.impl.IntSetting

object Settings {
    val REVENGE_EXPIRE_TIME = IntSetting("復讐の時間", default = 1800, min = 0, description = "復讐が終わるまでの時間をtick単位で指定できます。")
    val REVENGE_DAMAGE_MULTIPLIER = DoubleSetting("復讐のダメージ倍率", default = 1.5, min = 0.0, description = "復讐の相手に与えるダメージ倍率を指定できます。")
    val COMBAT_TAG_TIME = IntSetting("Combat Tagの時間", default = 60000,min = 0, description = "Combat Tagの持続時間を変更できます。")
}