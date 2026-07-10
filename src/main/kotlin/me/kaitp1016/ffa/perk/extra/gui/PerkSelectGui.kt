package me.kaitp1016.ffa.perk.extra.gui

import me.kaitp1016.ffa.mc
import me.kaitp1016.ffa.packetgui.AbstractPacketGui
import me.kaitp1016.ffa.packetgui.ChestPacketGui
import me.kaitp1016.ffa.perk.PerkAttributeHandler
import me.kaitp1016.ffa.perk.extra.ExtraPerk
import me.kaitp1016.ffa.perk.extra.ExtraPerkAttributeHandler
import me.kaitp1016.ffa.perk.extra.ExtraPerkManager
import me.kaitp1016.ffa.perk.extra.ExtraPerks
import me.kaitp1016.ffa.utils.DatapackAPI.getMoney
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.item.component.TooltipDisplay

class PerkSelectGui: ChestPacketGui {
    override val displayName = net.kyori.adventure.text.Component.text("パーク")
    override val name = "Perk Main"

    var index: Int
    val perkData: ExtraPerkManager.PlayerExtraPerkData
    val perks:List<ExtraPerk>

    constructor(player: ServerPlayer, index: Int, parent: AbstractPacketGui?) : super(player, 36) {
        this.parent = parent
        this.index = index
        this.perkData = ExtraPerkManager.getPerkData(player.uuid)
        this.perks = ExtraPerks.entries()
    }

    override fun onOpen() {
        items.fill(ItemStack(Items.GRAY_STAINED_GLASS_PANE).apply {
            set(DataComponents.ITEM_NAME, Component.empty())
            set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay(true,linkedSetOf()))
        })

        perks.forEachIndexed { index, perk ->
            setItem(index, getPerkItem(perk))
        }

        setOpenParentItem(32)

        super.onOpen()
    }

    override fun onClick(packet: ServerboundContainerClickPacket) {
        val slot = packet.slotNum.toInt()
        val perk = perks.getOrNull(slot)
        if (perk == null) {
            update(false)
            return
        }

        if (!isUnlocked(perk)) {
            val cost = perk.cost
            if (player.getMoney() < cost) {
                player.sendSystemMessage(Component.literal("ポイントが足りません!"))
                player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.SPLASH_POTION_BREAK), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))
                update(false)
                return
            }

            ConfirmBuyPerkGui(player,perk,this).open()
            player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.EXPERIENCE_ORB_PICKUP), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))

            return
        }

        mc.execute {
            if (!perk.canDuplicate && perkData.selectedPerks.contains(perk)) {
                player.sendSystemMessage(Component.literal("このパークは重複できません!!"))
                player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.SPLASH_POTION_BREAK), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))
                update(false)
                return@execute
            }

            this.perkData.selectedPerks[index] = perk
            player.connection.send(ClientboundSoundPacket(Holder.direct(SoundEvents.EXPERIENCE_ORB_PICKUP), SoundSource.MASTER, player.x, player.y, player.z, 1f, 1f, 1L))
            openParent()
            ExtraPerkAttributeHandler.update(player.bukkitEntity)
        }
    }

    private fun getPerkItem(perk: ExtraPerk): ItemStack {
        if (!isUnlocked(perk)) {
            return ItemStack(Items.RED_STAINED_GLASS_PANE).apply {
                set(DataComponents.ITEM_NAME, Component.literal(perk.name).withColor(0xFFFF55))
                set(DataComponents.LORE, ItemLore(perk.description.map { Component.literal(it).withStyle(Style.EMPTY.withItalic(false).withColor(0xFFFFFF)) }.toMutableList().also {
                    it.add(Component.empty())
                    it.add(Component.literal("必要ポイント: ${perk.cost}").withStyle(Style.EMPTY.withItalic(false).withColor(0xFFFFFF)))
                }.toList()))
            }
        }


        return perk.icon.copy().apply {
            set(DataComponents.ITEM_NAME, Component.literal(perk.name).withColor(0x55FF55))
            set(DataComponents.LORE, ItemLore(perk.description.map { Component.literal(it).withStyle(Style.EMPTY.withItalic(false).withColor(0xFFFFFF)) }))
        }
    }

    private fun isUnlocked(perk: ExtraPerk): Boolean {
        return this.perkData.unlockedPerks.contains(perk)
    }
}