package me.kaitp1016.ffa.utils

import me.kaitp1016.ffa.mc
import net.minecraft.network.chat.Component
import net.minecraft.server.commands.ScoreboardCommand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.scores.ScoreHolder
import net.minecraft.world.scores.criteria.ObjectiveCriteria

object DatapackAPI {
    val OBJECTIVE_MONEY = mc.scoreboard.getObjective("money") ?: mc.scoreboard.addObjective("money", ObjectiveCriteria.DUMMY, Component.literal("money"), ObjectiveCriteria.RenderType.INTEGER,false,null)
    val OBJECTIVE_PRESTIGE = mc.scoreboard.getObjective("prestige") ?: mc.scoreboard.addObjective("prestige", ObjectiveCriteria.DUMMY, Component.literal("prestige"), ObjectiveCriteria.RenderType.INTEGER,false,null)

    fun Player.addMoney(amount: Int): Int {
        val holder = ScoreHolder.fromGameProfile(this.gameProfile)
        val score = mc.scoreboard.getOrCreatePlayerScore(holder,OBJECTIVE_MONEY,false)
        score.add(amount)
        return score.get()
    }

    fun Player.getMoney(): Int {
        val holder = ScoreHolder.fromGameProfile(this.gameProfile)
        val score = mc.scoreboard.getOrCreatePlayerScore(holder,OBJECTIVE_MONEY,false)
        return score.get()
    }

    fun Player.getPrestige(): Int {
        val holder = ScoreHolder.fromGameProfile(this.gameProfile)
        val score = mc.scoreboard.getOrCreatePlayerScore(holder,OBJECTIVE_PRESTIGE,false)
        return score.get()
    }
}