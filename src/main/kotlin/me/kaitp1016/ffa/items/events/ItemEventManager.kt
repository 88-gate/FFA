package me.kaitp1016.ffa.items.events

import me.kaitp1016.ffa.items.CustomItem
import java.lang.reflect.Method

object  ItemEventManager {
    val listeners: MutableMap<Class<out ItemEvent>, MutableList<Listener>> = mutableMapOf()

    data class Listener(val item: CustomItem, val itemId: Int, val method: Method)

    fun register(item: CustomItem) {
        item::class.java.methods.forEach {method ->
            if (!method.isAnnotationPresent(ItemEventHandler::class.java)) return@forEach

            val event: Class<out ItemEvent> = method.parameters[0].type as Class<out ItemEvent>
            val array = listeners.getOrPut(event) { ArrayList() }

            array.add(Listener(item, item.internalId,method))
        }
    }

    fun post(event: ItemEvent,itemId: Int) {
        val listeners = listeners[event::class.java]
        if (listeners == null) return

        listeners.forEach {
            if (it.itemId == itemId) {
                it.method.invoke(it.item,event)
            }
        }
    }
}