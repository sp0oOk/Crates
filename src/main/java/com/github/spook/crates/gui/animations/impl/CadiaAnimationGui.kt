package com.github.spook.crates.gui.animations.impl

import com.github.spook.crates.entity.Crate
import com.github.spook.crates.entity.Crates
import com.github.spook.crates.entity.MConf
import com.github.spook.crates.entity.MPlayer
import com.github.spook.crates.gui.animations.Animated
import com.github.spook.crates.gui.animations.EndReason
import com.massivecraft.massivecore.chestgui.RefreshableChestGui
import com.massivecraft.massivecore.mixin.MixinMessage
import com.massivecraft.massivecore.util.ItemBuilder
import com.massivecraft.massivecore.util.Txt
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*
import java.util.stream.IntStream

/**
 * An animation replicating how normal crates work on Minecadia
 * pretty simple, just a bunch of glass panes and a random reward
 *
 * @author spook
 *
 * Updates:
 * - 10/06/2023: spook - Initial creation.
 */
class CadiaAnimationGui(private val crate: Crate) :
    RefreshableChestGui(
        Bukkit.createInventory(null, 9 * 3, Txt.parse("Opening crate: ${crate.displayName}")),
        null
    ), Animated {

    private var player: Player? = null

    // Animation settings
    private val cycles: Int = 5
    private var currentTick: Int = 0
    private var completed: Boolean = false

    init {
        isAutoclosing = false
        setAutoRefreshing(2) // 1/10th of a second
        runnablesClose.add(Runnable { if (!completed) end(EndReason.PLAYER_CLOSE_OR_QUIT) })
    }

    /**
     * Main Animation loop.
     */

    override fun refresh() {
        if (currentTick % 5 == 0) {
            setRandomGlassPanes()
        } else {
            when (currentTick % 5) {
                1 -> setAnimated(0, 18, 8, 26, 9, 17)
                2 -> setAnimated(1, 19, 7, 25, 10, 16)
                3 -> setAnimated(2, 20, 6, 24, 11, 15)
                4 -> setAnimated(3, 21, 5, 23, 12, 14)
            }
        }

        val reward = Crates.get().getNextReward(crate.crateId);
        inventory.setItem(13, reward!!.toItem())
        currentTick++;

        if (currentTick == cycles * 5 + 5) {
            reward.win(player!!)
            completed = true
            player!!.closeInventory()
            end(EndReason.ANIMATION_END)
        }

    }

    /**
     * Starts the animation for the player.
     *
     * @param player The player to start the animation for.
     */

    override fun start(player: Player) {
        this.player = player
        open(player)
    }

    /**
     * Ends the animation for the player.
     *
     * @param endReason The reason the animation ended.
     */

    override fun end(endReason: EndReason) {
        if (endReason == EndReason.PLAYER_CLOSE_OR_QUIT) {
            // If player isn't online, and we haven't completed the animation, refund the key.
            if (!player!!.isOnline && !completed) {
                MPlayer.get(player!!)!!.addKeys(crate.crateId, 1)
                com.github.spook.crates.CratesPlugin.instance.log("Player ${player!!.name} unexpectedly closed crate animation gui refunding 1x ${crate.displayName} key.")
                return
            }

            // If they are online, forfeit whatever reward they would have gotten and go roulette mode. :)
            val randomReward = Crates.get().getNextReward(crate.crateId)
            randomReward!!.win(player!!)

            // Acknowledge the player that they forfeited their reward.
            MixinMessage.get()
                .messageOne(
                    player,
                    Txt.parse(MConf.i.forfeitedAnimation.replace("{prefix}", MConf.i.prefix))
                )
        }
    }

    /**
     * Sets the animated glass panes.
     *
     * @param slots The slots to set the animated glass panes.
     */

    private fun setAnimated(vararg slots: Int) {
        slots.forEach {
            inventory.setItem(
                it, ItemBuilder(Material.STAINED_GLASS_PANE)
                    .displayname("")
                    .durability(0)
                    .build()
            )
        }
    }

    /**
     * Set Random Glass Panes
     */

    private fun setRandomGlassPanes() {
        val color = getRandomColor()
        IntStream.range(0, 27).forEach {
            inventory.setItem(
                it, ItemBuilder(Material.STAINED_GLASS_PANE)
                    .displayname("")
                    .durability(color)
                    .build()
            )
        }
    }

    /**
     * Get a random stained-glass pane color.
     */

    private fun getRandomColor(): Short {
        return (Random().nextInt(15) + 1).toShort()
    }

}