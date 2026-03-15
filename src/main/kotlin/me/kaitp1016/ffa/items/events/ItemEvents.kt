package me.kaitp1016.ffa.items.events

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent
import net.minecraft.world.damagesource.DamageSource
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack

object ItemEvents {
    class UseEvent: CancellableItemEvent {
        val bukkitEvent: PlayerInteractEvent

        constructor(item: ItemStack,player: Player,bukkitEvent: PlayerInteractEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }

    class SwingEvent: ItemEvent {
        val bukkitEvent: PlayerInteractEvent

        constructor(item: ItemStack,player: Player,bukkitEvent: PlayerInteractEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }

    class ConsumeEvent: CancellableItemEvent {
        val bukkitEvent: PlayerItemConsumeEvent

        constructor(item: ItemStack,player: Player,bukkitEvent: PlayerItemConsumeEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }

    class DamageEntityEvent: CancellableItemEvent {
        val bukkitEvent: EntityDamageByEntityEvent

        constructor(item: ItemStack,player: Player,bukkitEvent: EntityDamageByEntityEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }

    class BlockDropItemEven: CancellableItemEvent {
        val bukkitEvent: BlockDropItemEvent

        constructor(item: ItemStack,player: Player,bukkitEvent: BlockDropItemEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }

    class BlockPlaceEvent: CancellableItemEvent {
        val bukkitEvent: org.bukkit.event.block.BlockPlaceEvent

        constructor(item: ItemStack,player: Player,bukkitEvent: org.bukkit.event.block.BlockPlaceEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }

    class TickWhileHolding: ItemEvent {
        constructor(item: ItemStack,player: Player):super(item,player) {

        }
    }

    class ShootEvent: CancellableItemEvent {
        val bukkitEvent: EntityShootBowEvent

        constructor(item: ItemStack,player: Player,bukkitEvent: EntityShootBowEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }

    class TridentThrowEvent: CancellableItemEvent {
        val bukkitEvent: PlayerLaunchProjectileEvent

        constructor(item: ItemStack,player: Player,bukkitEvent: PlayerLaunchProjectileEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }

    class KillEvent: CancellableItemEvent {
        val bukkitEvent: EntityDeathEvent
        val damageSource: DamageSource

        constructor(item: ItemStack, player:Player, damageSource: DamageSource, bukkitEvent: EntityDeathEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
            this.damageSource = damageSource
        }
    }

    class SwapItemInInventoryEvent: CancellableItemEvent {
        val bukkitEvent: InventoryClickEvent

        constructor(item: ItemStack, player:Player, bukkitEvent: InventoryClickEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }

    class ClickItemInInventoryEvent: CancellableItemEvent {
        val bukkitEvent: InventoryClickEvent

        constructor(item: ItemStack, player:Player, bukkitEvent: InventoryClickEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }

    class DamageWhileUsingEvent: CancellableItemEvent {
        val bukkitEvent: EntityDamageEvent

        constructor(item: ItemStack,player: Player,bukkitEvent: EntityDamageEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }

    class FishEvent: CancellableItemEvent {
        val bukkitEvent: PlayerFishEvent

        constructor(item: ItemStack,player: Player,bukkitEvent: PlayerFishEvent):super(item,player) {
            this.bukkitEvent = bukkitEvent
        }
    }
}
