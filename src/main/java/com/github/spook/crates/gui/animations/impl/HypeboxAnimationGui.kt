package com.github.spook.crates.gui.animations.impl

import com.github.spook.crates.entity.*
import com.github.spook.crates.gui.animations.Animated
import com.github.spook.crates.gui.animations.EndReason
import com.github.spook.crates.objects.Reward
import com.massivecraft.massivecore.chestgui.RefreshableChestGui
import com.massivecraft.massivecore.mson.Mson
import com.massivecraft.massivecore.util.ItemBuilder
import com.massivecraft.massivecore.util.Txt
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.metadata.FixedMetadataValue
import java.lang.ref.WeakReference

/**
 * This is the hypebox animation gui.
 *
 * @author spook
 *
 * Updates:
 * - 10/07/2023: spook - Initial creation.
 * - 10/08/2023: spook - Bunch of fixes and added refunding.
 * - 10/09/2023: spook - Recoded start() method to be cleaner
 */
class HypeboxAnimationGui(private val hypebox: Hypebox) :
    RefreshableChestGui(
        Bukkit.createInventory(
            null,
            27,
            "Opening Hypebox: " + Txt.parse(hypebox.displayName), // Default in-case player display name is longer than 32 chars
        ), null
    ), Animated {

    private var owner: Player? = null

    // Animation settings (typically common place for these)
    private val cycles: Int = 10
    private val animationSpeed: Int = 2

    // Reward cache and cached viewers
    private val rewardCache: MutableList<Reward?> = mutableListOf()
    private var cachedViewers: List<WeakReference<Player>> = listOf()

    // Animation variables
    private var tick: Int = 0
    private var completed: Boolean = false

    init {
        isAutoclosing = false
        setAutoRefreshing(animationSpeed)
        runnablesClose.add(Runnable { end(EndReason.PLAYER_CLOSE_OR_QUIT) })
    }

    /**
     * Main refresh method
     */

    override fun refresh() {
        // If animation isn't completed in main loo
        if (!completed) {

            // Set placeholder items (top and bottom rows) if not set
            if (inventory.getItem(0) == null) {
                (getRow(0) + getRow(2)).forEach { slot ->
                    if (slot != 4) {
                        inventory.setItem(
                            slot, ItemBuilder(Material.STAINED_GLASS_PANE)
                                .displayname(" ").glow().durability(7).build()
                        )
                    }
                }
                inventory.setItem(4, buildSkull(owner!!))
            }

            // If no starting items are set, set them (random 9 rewards in CSGO slots)
            if (inventory.getItem(9) == null) {
                repeat(9) {
                    rewardCache.add(Hypeboxes.get().getNextReward(hypebox.hypeboxId))
                }
                getRow(1).forEach {
                    inventory.setItem(
                        it,
                        rewardCache[it - 9]?.toItem() ?: ItemStack(Material.AIR)
                    )
                }
            }

            // shift rewards to the left, removing the first item and adding a new one to the end
            // wish I had a better way to do this, but I don't at-least I cba to think of one
            rewardCache.removeAt(0)
            rewardCache.add(Hypeboxes.get().getNextReward(hypebox.hypeboxId))

            // Update the inventory shifting the rewards to the left
            getRow(1).forEach {
                inventory.setItem(
                    it,
                    rewardCache[it - 9]?.toItem() ?: ItemStack(Material.AIR)
                )
            }

            // Increment tick
            tick++

            // Check if we can end the animation
            if (tick == cycles * 5) {
                completed = true
                end(EndReason.ANIMATION_END)
            }
        }
    }

    /**
     * Start the animation for a player.
     *
     * @param player The player to start the animation for.
     */

    override fun start(player: Player) {
        if (owner == null) {
            player.setMetadata("crate_hypebox", FixedMetadataValue(com.github.spook.crates.CratesPlugin.instance, this))
            owner = player
            announce()
        }

        if (player != owner) {
            cachedViewers += WeakReference(player)
        }

        open(player)
        com.github.spook.crates.util.TitleUpdater.update(
            player,
            Txt.parse(PlaceholderAPI.setPlaceholders(owner, MConf.i.hypeboxPreviewTitle))
        )
    }

    /**
     * End the animation.
     *
     * @param endReason The reason the animation ended.
     */

    override fun end(endReason: EndReason) {
        if (endReason == EndReason.ANIMATION_END) {
            owner!!.removeMetadata("crate_hypebox", com.github.spook.crates.CratesPlugin.instance)
            rewardCache[4]!!.win(owner!!, hypebox.displayName, true)
            Bukkit.getScheduler().runTaskLater(
                com.github.spook.crates.CratesPlugin.instance,
                { closeAll() },
                40L
            )
            return
        }

        if (completed) {
            return;
        }

        closeAll()

        val random = Hypeboxes.i.getNextReward(hypebox.hypeboxId)

        owner!!.isOnline
            .takeIf { it }
            ?.let {
                owner!!.removeMetadata("crate_hypebox", com.github.spook.crates.CratesPlugin.instance)
                random!!.win(owner!!, hypebox.displayName, true)
            }
    }


    /**
     * Announce the crate opening to all players.
     */

    private fun announce() {

        val contentsColored = MConf.i.hypeboxAnnouncement
        val regex = Regex(pattern = "(.*?)\\Q${MConf.i.hypeboxJsonSelector}\\E(.*)")
        val matchResult = regex.find(contentsColored)

        if (matchResult != null) {
            val (beforeSelector, afterSelector) = matchResult.destructured

            Mson.mson("\n")
                .add(replace(beforeSelector))
                .add("${MConf.i.hypeboxSelectorColor}${ChatColor.BOLD}${MConf.i.hypeboxJsonSelector}")
                .tooltip(replace(MConf.i.hypeboxJsonTooltip))
                .command("/crate watch ${owner!!.name}")
                .add(replace(afterSelector))
                .add("\n")
                .messageAll()
        }
    }


    /**
     * Build a skull itemstack for the owner of the crate.
     *
     * @param player The player to build the skull for.
     * @return The skull itemstack.
     */

    private fun buildSkull(player: Player): ItemStack {
        val skull = ItemStack(Material.SKULL_ITEM, 1, 3)
        val meta = skull.itemMeta as SkullMeta
        meta.displayName = "${ChatColor.YELLOW}${ChatColor.BOLD}${player.name}"
        meta.owner = player.name
        skull.itemMeta = meta
        return skull
    }

    /**
     * Get a row of slots.
     *
     * NOTE: could have done this better with a loop but I cba.
     *
     * @param row The row to get.
     * @return The slots in the row.
     */

    private fun getRow(row: Int): List<Int> {
        return when (row) {
            1 -> listOf(9, 10, 11, 12, 13, 14, 15, 16, 17)
            2 -> listOf(18, 19, 20, 21, 22, 23, 24, 25, 26)
            else -> listOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        }
    }

    /**
     * Quick method to replace placeholders in a string.
     *
     * @param str The string to replace placeholders in.
     * @return The string with placeholders replaced.
     */

    private fun replace(str: String): String {
        return Txt.parse(
            com.github.spook.crates.CratesPlugin
                .instance
                .parse(
                    str,
                    "{player-name}",
                    owner!!.name,
                    "{player-formatted-display}",
                    PlaceholderAPI.setPlaceholders(owner!!, MConf.i.hypeboxPreviewTitle),
                    "{crate-display-name}",
                    hypebox.displayName
                )
        )
    }

    /**
     * Close for all viewers, then the owner stopping the gui from
     * being untracked by MassiveCore. and people being able to
     * take items from the gui.
     */

    private fun closeAll() {
        cachedViewers.forEach {
            it.get()?.closeInventory()
        }
        owner?.closeInventory()
    }

}
