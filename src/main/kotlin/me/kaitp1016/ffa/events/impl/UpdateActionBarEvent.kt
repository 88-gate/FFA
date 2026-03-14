package me.kaitp1016.ffa.events.impl

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class UpdateActionBarEvent(player: Player) : PlayerEvent(player), Cancellable {
    private var cancel = false

    var actionBar = ""
    var times = 0

    fun addText(text: String) {
        if (times > 0) {
            actionBar += " §7| §a"
        }
        actionBar += text
        times++
    }

    override fun getHandlers(): HandlerList {
        return handler
    }

    override fun setCancelled(state: Boolean) {
        this.cancel = state
    }

    override fun isCancelled(): Boolean {
        return this.cancel
    }

    companion object {
        val handler = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handler
        }
    }
}