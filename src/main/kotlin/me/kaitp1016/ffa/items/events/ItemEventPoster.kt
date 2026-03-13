package me.kaitp1016.ffa.items.events

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent
import me.kaitp1016.ffa.events.impl.TickEvent
import me.kaitp1016.ffa.items.ItemManager.isBattleRoyalItem
import me.kaitp1016.ffa.utils.NMSUtils.asCraftDamageSource
import me.kaitp1016.ffa.utils.NMSUtils.asCraftPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.entity.Trident
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent

object ItemEventPoster: Listener {
    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        when (event.action) {
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                val item = event.item
                if (item == null || !item.isBattleRoyalItem()) return

                ItemEvents.SwingEvent(item,event.player,event).post()
            }
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                val item = event.item
                if (item == null || !item.isBattleRoyalItem()) return

                val itemEvent = ItemEvents.UseEvent(item,event.player,event)
                itemEvent.post()

                if (itemEvent.isCancelled) {
                    event.isCancelled = true
                }
            }
            else -> Unit
        }
    }

    @EventHandler
    fun onConsume(event: PlayerItemConsumeEvent) {
        val item = event.item
        if (!item.isBattleRoyalItem()) return

        val itemEvent = ItemEvents.ConsumeEvent(item,event.player,event)
        itemEvent.post()

        if (itemEvent.isCancelled) {
            event.isCancelled = true
        }

    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val player = event.damager
        if (player !is Player) return

        val item = player.inventory.itemInMainHand
        if (!item.isBattleRoyalItem()) return

        val itemEvent = ItemEvents.DamageEntityEvent(item,player,event)
        itemEvent.post()

        if (itemEvent.isCancelled) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val player = event.entity
        if (player !is Player) return

        player.inventory.armorContents.forEach {item ->
            if (item?.isBattleRoyalItem() != true) return@forEach
            val itemEvent = ArmorEvents.DamageEvent(item,player,event)
            itemEvent.post()

            if (itemEvent.isCancelled) {
                event.isCancelled = true
            }
        }

        val usingItem = player.asCraftPlayer().handle.useItem.bukkitStack
        if (usingItem.isBattleRoyalItem()) {
            val itemEvent = ItemEvents.DamageWhileUsingEvent(usingItem,player,event)
            itemEvent.post()

            if (itemEvent.isCancelled) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun noEntityDeath(event: EntityDeathEvent) {
        val bukkitSource = event.damageSource.asCraftDamageSource()
        val source = bukkitSource.handle
        val player = bukkitSource.causingEntity
        if (player !is Player) return

        val item = source.weaponItem?.bukkitStack ?: return
        if (!item.isBattleRoyalItem()) return

        val itemEvent = ItemEvents.KillEvent(item,player,source,event)
        itemEvent.post()

        if (itemEvent.isCancelled) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockDropItem(event: BlockDropItemEvent) {
        val player = event.player

        val item = event.player.inventory.itemInMainHand
        if (!item.isBattleRoyalItem()) return

        val itemEvent = ItemEvents.BlockDropItemEven(item,player,event)
        itemEvent.post()

        if (itemEvent.isCancelled) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlaceBlock(event: BlockPlaceEvent) {
        val player = event.player
        val item = event.itemInHand

        if (!item.isBattleRoyalItem()) return

        val itemEvent = ItemEvents.BlockPlaceEvent(item,player,event)
        itemEvent.post()

        if (itemEvent.isCancelled) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onFish(event: PlayerFishEvent) {
        val player = event.player
        val hand = event.hand ?: return
        val item = player.inventory.getItem(hand)

        if (!item.isBattleRoyalItem()) return

        val itemEvent = ItemEvents.FishEvent(item,player,event)
        itemEvent.post()

        if (itemEvent.isCancelled) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onShoot(event: EntityShootBowEvent) {
        val item = event.bow
        val player = event.entity
        if (player !is Player || item == null || !item.isBattleRoyalItem()) return

        val itemEvent = ItemEvents.ShootEvent(item,player,event)
        itemEvent.post()

        if (itemEvent.isCancelled) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onProjectileLaunch(event: PlayerLaunchProjectileEvent) {
        val entity = event.projectile
        val player = Bukkit.getPlayer(entity.ownerUniqueId ?: return)
        if (player !is Player || entity !is Trident) return

        val item = entity.itemStack
        val itemEvent = ItemEvents.TridentThrowEvent(item,player,event)
        itemEvent.post()

        if (itemEvent.isCancelled) {
            event.isCancelled = true
        }
    }

    var tick = 0
    @EventHandler
    fun onTick(event: TickEvent) {
        Bukkit.getOnlinePlayers().forEach{player ->
            val item = player.inventory.itemInMainHand
            if (!item.isBattleRoyalItem()) return@forEach

            ItemEvents.TickWhileHolding(item,player).post()
        }

        tick++
        if (tick >= 20) {
            tick = 0

            Bukkit.getOnlinePlayers().forEach{player ->
                val item = player.inventory.itemInMainHand
                if (!item.isBattleRoyalItem()) return@forEach

                ItemEvents.SecoundWhileHolding(item,player).post()
            }
        }
    }


    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked
        if (player !is Player) return

        val cursor = event.cursor
        if (cursor.isBattleRoyalItem()) {
            val itemEvent = ItemEvents.SwapItemInInventoryEvent(cursor,player,event)
            itemEvent.post()

            if (itemEvent.isCancelled) {
                event.isCancelled = true
            }
        }

        val item = event.currentItem
        if (item != null && item.isBattleRoyalItem()) {
            val itemEvent = ItemEvents.ClickItemInInventoryEvent(item,player,event)
            itemEvent.post()

            if (itemEvent.isCancelled) {
                event.isCancelled = true
            }
        }
    }
}