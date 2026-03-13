package me.kaitp1016.ffa.utils

import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.minecraft.network.protocol.Packet
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.block.CraftBlock
import org.bukkit.craftbukkit.block.CraftBlockState
import org.bukkit.craftbukkit.damage.CraftDamageSource
import org.bukkit.craftbukkit.entity.CraftEntity
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.damage.DamageSource
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import net.minecraft.world.item.ItemStack as MCItemStack

object NMSUtils {
    fun Player.asCraftPlayer(): CraftPlayer {
        return this as CraftPlayer
    }

    fun Entity.asCraftEntity(): CraftEntity {
        return this as CraftEntity
    }

    fun Player.sendPacket(packet: Packet<*>) {
        this.asCraftPlayer().handle.connection.send(packet)
    }

    fun World.asCraftWorld(): CraftWorld {
        return this as CraftWorld
    }

    fun Block.asCraftBlock(): CraftBlock {
        return this as CraftBlock
    }

    fun BlockState.asCraftBlockState(): CraftBlockState {
        return this as CraftBlockState
    }

    fun DamageSource.asCraftDamageSource(): CraftDamageSource {
        return this as CraftDamageSource
    }

    fun ItemStack.asCraftItemStack(): CraftItemStack {
        if (this is CraftItemStack && this.handle != null) {
            return this
        }
        else {
            return MCItemStack.fromBukkitCopy(this).bukkitStack as CraftItemStack
        }
    }

    fun Component.toMCComponent(): net.minecraft.network.chat.Component {
        return PaperAdventure.asVanilla(this)
    }
}