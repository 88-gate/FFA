package me.kaitp1016.ffa.events.impl

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class TickEvent: Event() {
    override fun getHandlers(): HandlerList {
        return handler
    }

    companion object {
        val handler = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handler
        }
    }
}