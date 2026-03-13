package me.kaitp1016.ffa

import com.google.gson.GsonBuilder
import com.mojang.datafixers.util.Either
import me.kaitp1016.ffa.commands.FFACommand
import me.kaitp1016.ffa.events.EventManager
import me.kaitp1016.ffa.game.CombatTag
import me.kaitp1016.ffa.game.Revenge
import me.kaitp1016.ffa.items.ItemManager
import me.kaitp1016.ffa.items.events.ItemEventPoster
import me.kaitp1016.ffa.packetgui.PacketGuiManager
import me.kaitp1016.ffa.setting.Settings
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import me.kaitp1016.ffa.utils.RecipeUtils
import me.kaitp1016.ffa.utils.Scheduler
import net.minecraft.network.protocol.common.ClientboundServerLinksPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.ServerLinks
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class FFA : JavaPlugin(), Listener {
    override fun onEnable() {
        plugin = this

        listOf(EventManager,Scheduler, ItemEventPoster, PacketGuiManager, Revenge, CombatTag, this)
            .forEach { server.pluginManager.registerEvents(it,plugin) }

        Settings
        ItemManager.registeAll()

        this.getCommand("ffa")!!.apply {
            this.setExecutor(FFACommand)
            this.tabCompleter = FFACommand
        }

        RecipeUtils.updateAllData()
    }

    override fun onDisable() {

    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player.asCraftPlayer().handle

        player.connection.send(
            ClientboundServerLinksPacket(
                listOf(
                    ServerLinks.UntrustedEntry(Either.left(ServerLinks.KnownLinkType.ANNOUNCEMENTS), "https://discord.gg/2PGFuasqFM"),
                    ServerLinks.UntrustedEntry(Either.left(ServerLinks.KnownLinkType.FEEDBACK), "https://discord.gg/2PGFuasqFM"),
                    ServerLinks.UntrustedEntry(Either.left(ServerLinks.KnownLinkType.COMMUNITY), "https://discord.gg/2PGFuasqFM"),
                    ServerLinks.UntrustedEntry(Either.left(ServerLinks.KnownLinkType.STATUS), "https://discord.gg/2PGFuasqFM"),
                    ServerLinks.UntrustedEntry(Either.left(ServerLinks.KnownLinkType.SUPPORT), "https://discord.gg/2PGFuasqFM"),
                    ServerLinks.UntrustedEntry(Either.left(ServerLinks.KnownLinkType.WEBSITE), "https://discord.gg/2PGFuasqFM"),
                    ServerLinks.UntrustedEntry(Either.left(ServerLinks.KnownLinkType.BUG_REPORT), "https://discord.gg/2PGFuasqFM"),
                )
            )
        )
    }
}

lateinit var plugin: JavaPlugin
    private set

val mc: MinecraftServer = MinecraftServer.getServer()
val gson = GsonBuilder().apply {
    this.setPrettyPrinting()
}.create()!!