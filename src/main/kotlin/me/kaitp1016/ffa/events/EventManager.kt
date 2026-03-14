package me.kaitp1016.ffa.events

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import me.kaitp1016.ffa.events.impl.PacketReciveEvent
import me.kaitp1016.ffa.events.impl.PacketSendEvent
import me.kaitp1016.ffa.events.impl.SecoundEvent
import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.events.impl.UpdateActionBarEvent
import me.kaitp1016.ffa.plugin
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import net.kyori.adventure.text.Component
import net.minecraft.network.protocol.Packet
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object EventManager: Listener {
    init {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            Bukkit.getPluginManager().callEvent(SecoundEvent())
        }, 0, 20)

        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            Bukkit.getPluginManager().callEvent(TickEvent())
        }, 0, 0)

        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            Bukkit.getOnlinePlayers().forEach {
                val event = UpdateActionBarEvent(it)
                Bukkit.getPluginManager().callEvent(event)

                if (!event.isCancelled) it.sendActionBar(Component.text(event.actionBar))
            }
        }, 0, 2)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        player.asCraftPlayer().handle.connection.connection.channel.pipeline().addBefore("packet_handler","battleroyal_packet_listener_${player.name}", PacketListener(player))
    }

    class PacketListener: ChannelDuplexHandler {
        val player: Player

        constructor(player: Player) {
            this.player = player
        }

        override fun channelRead(ctx: ChannelHandlerContext?, packet: Any?) {
            if (packet is Packet<*>) {
                val event = PacketReciveEvent(packet, player)
                Bukkit.getPluginManager().callEvent(event)

                if (!event.isCancelled) {
                    super.channelRead(ctx, packet)
                }
            }
        }

        override fun write(ctx: ChannelHandlerContext?, packet: Any?, promise: ChannelPromise?) {
            if (packet is Packet<*>) {
                val event = PacketSendEvent(packet, player)
                Bukkit.getPluginManager().callEvent(event)

                if (!event.isCancelled) {
                    super.write(ctx, packet, promise)
                }
            }
        }
    }
}