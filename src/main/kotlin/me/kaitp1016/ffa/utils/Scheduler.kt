package me.kaitp1016.ffa.utils

import me.kaitp1016.ffa.events.impl.TickEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.lang.Exception
import java.util.concurrent.CopyOnWriteArraySet

object Scheduler: Listener {
    private class Task (var ticksLeft: Int,val task: () -> Unit)

    private val scheduledTasks = CopyOnWriteArraySet<Task>()

    private var isProcessing = false
    private var toAdd: MutableList<Task>? = null

    fun scheduleTask(tick: Int, task:() -> Unit) {
        if (isProcessing) {
            toAdd!!.add(Task(tick, task))
        } else {
            scheduledTasks.add(Task(tick, task))
        }
    }

    @EventHandler
    fun onTick(event: TickEvent) {
        isProcessing = true
        toAdd = mutableListOf()

        scheduledTasks.removeIf {
            it.ticksLeft--
            if (it.ticksLeft < 1) {
                try {
                    it.task()
                }
                catch (exception: Exception) {
                    exception.printStackTrace()
                }

                return@removeIf true
            }

            return@removeIf false
        }

        scheduledTasks.addAll(toAdd!!)
        toAdd = null

        isProcessing = false
    }
}