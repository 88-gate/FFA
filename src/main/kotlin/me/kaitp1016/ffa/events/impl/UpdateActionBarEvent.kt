package me.kaitp1016.ffa.events.impl

import net.minecraft.network.chat.Component
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class UpdateActionBarEvent(player: Player) : PlayerEvent(player), Cancellable {
    private var cancel = false

    var actionBar = Component.empty()
    var times = 0

    fun addText(text: String) {
        if (times > 0) {
            actionBar.append(Component.literal(" §7| §a"))
        }
        actionBar.append(Component.literal(text))
        times++
    }

    fun addComponent(component: Component) {
        if (times > 0) {
            actionBar.append(Component.literal(" §7| §a"))
        }

        actionBar.append(component)
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