package me.kaitp1016.ffa.utils

import me.kaitp1016.ffa.mc
import net.minecraft.world.entity.player.Player
import net.minecraft.world.scores.ScoreHolder

object DatapackAPI {
    val OBJECTIVE_MONEY = mc.scoreboard.getObjective("money")!!

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
}