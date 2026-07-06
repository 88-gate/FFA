package me.kaitp1016.ffa.utils

import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.text.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity as MCEntity
import net.minecraft.world.level.block.state.BlockState as MCBlockState
import net.minecraft.world.damagesource.DamageSource as MCDamageSource
import net.minecraft.network.chat.Component as MCComponent
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
    fun Player.toMC(): ServerPlayer {
        return (this as CraftPlayer).handle
    }

    fun Entity.toMC(): MCEntity {
        return (this as CraftEntity).handle
    }

    fun Player.sendPacket(packet: Packet<*>) {
        this.toMC().connection.send(packet)
    }

    fun World.toMC(): ServerLevel {
        return (this as CraftWorld).handle
    }

    fun Block.toCraft(): CraftBlock {
        return this as CraftBlock
    }

    fun Block.toMC(): MCBlockState {
        return (this as CraftBlock).nms
    }

    fun BlockState.toCraft(): CraftBlockState {
        return this as CraftBlockState
    }

    fun BlockState.toMC(): MCBlockState {
        return (this as CraftBlockState).handle
    }

    fun DamageSource.toMC(): MCDamageSource {
        return (this as CraftDamageSource).handle
    }

    fun ItemStack.asCraftItemStack(): CraftItemStack {
        if (this is CraftItemStack && this.handle != null) {
            return this
        } else {
            return MCItemStack.fromBukkitCopy(this).bukkitStack as CraftItemStack
        }
    }

    fun ItemStack.toMC(): MCItemStack {
        return (this as CraftItemStack).handle
    }

    fun ItemStack.toMCCopy(): MCItemStack {
        return CraftItemStack.asNMSCopy(this)
    }

    fun Component.toMCComponent(): MCComponent {
        return PaperAdventure.asVanilla(this)
    }
}