package me.kaitp1016.ffa.packetgui.impl.setting

import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.SignPacketGui
import me.kaitp1016.ffa.setting.Setting
import me.kaitp1016.ffa.utils.NMSUtils.toMCComponent
import me.kaitp1016.ffa.utils.Scheduler
import net.kyori.adventure.text.Component
import net.minecraft.server.level.ServerPlayer

class SetValueGui: SignPacketGui {
    override val name = "set value gui"
    val setting: Setting<*>

    constructor(player: ServerPlayer, setting: Setting<*>, parent: AbstractPacketGui):super(player,listOf(Component.text(""),Component.text("^^^^^^^"),Component.text("上に入力してね"),Component.text(""))) {
        this.parent = parent
        this.setting = setting
    }

    override fun onComplete(lines: List<String>) {
        Scheduler.scheduleTask(1) {
            val message = setting.parseAndSet(lines[0])
            if (message == null) {
                player.sendSystemMessage(Component.text("${setting.getValue()} に設定しました!").toMCComponent())
                parent?.open()
            }
            else {
                player.sendSystemMessage(Component.text("設定に失敗しました! $message").toMCComponent())
                parent?.open()
            }
        }
    }
}