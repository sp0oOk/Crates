package com.github.spook.crates.engine

import com.github.spook.crates.Perm
import com.github.spook.crates.entity.Crates
import com.github.spook.crates.entity.MConf
import com.github.spook.crates.gui.ChestPreviewGui
import com.massivecraft.massivecore.Engine
import com.massivecraft.massivecore.mixin.MixinMessage
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * This is the core engine that handles everything to do with
 * the opening, and altering of crates.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 * - 10/06/2023: spook - Added the removing of metadata keys on player quit.
 */
class EngineCrates : Engine() {

    companion object {
        private val i = EngineCrates()

        @JvmStatic
        fun get(): EngineCrates {
            return i
        }
    }


    /**
     * Checks if a block is near a crate.
     * Also, handles the placement of new crates.
     *
     * Emitted when a player places a block............. lol
     *
     * @param event The event.
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlace(event: BlockPlaceEvent) {

        if (event.block.type == MConf.i.crateType
            && isNearCrate(event.block)
        ) {
            MixinMessage.get().msgOne(
                event.player,
                MConf.i.cannotPlaceNearCrate.replace("{prefix}", MConf.i.prefix)
            )
            event.isCancelled = true
            return
        }

        val player = event.player

        if (!player.hasMetadata("crate_placing")) return
        val crateType = player.getMetadata("crate_placing")[0].asString()

        if (event.block.type != MConf.i.crateType) {
            MixinMessage.get().msgOne(
                player, MConf.i.invalidCrateBlock.replace("{prefix}", MConf.i.prefix)
                    .replace("{block-placed}", event.block.type.name)
                    .replace("{crate-block}", MConf.i.crateType.name)
            )
            return
        }

        Crates.i.setCrateLocation(event.block.location, crateType)
        player.removeMetadata("crate_placing", com.github.spook.crates.CratesPlugin.instance)
        MixinMessage.get().msgOne(
            player, MConf.i.cratePlaced.replace("{prefix}", MConf.i.prefix)
                .replace("{crate-type}", crateType)
        )
        Crates.i.syncHolograms()
    }

    /**
     * Handles survival breaking of crates, and checks if the player is sneaking.
     * before breaking the crate.
     *
     * @param event The event.
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBreakEvent(event: BlockBreakEvent) {
        event.block.takeIf { it.type == MConf.i.crateType }?.let { block ->
            Crates.i.crateAt(block.location)?.also { crate ->
                if (Perm.PLACE.has(event.player) && event.player.isSneaking) {
                    Crates.i.removeCrate(crate)
                    MixinMessage.get().msgOne(
                        event.player,
                        MConf.i.crateBroken.replace("{prefix}", MConf.i.prefix)
                            .replace("{crate-type}", crate.crateId)
                    )
                } else {
                    MixinMessage.get().msgOne(
                        event.player,
                        MConf.i.cannotBreakCrate.replace("{prefix}", MConf.i.prefix)
                    )
                    event.isCancelled = true
                }
            }
        }
    }

    /**
     * Handles the opening of crates, as-well
     * as the previewing of crates and creative mode breaking of crates.
     * since that does not trigger the break event.
     *
     * @param event The event.
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInteract(event: PlayerInteractEvent) {
        event.clickedBlock?.takeIf { it.type == MConf.i.crateType }?.let { clickedBlock ->
            Crates.i.crateAt(clickedBlock.location)?.also { crate ->
                event.isCancelled = true
                when (event.action) {
                    Action.RIGHT_CLICK_BLOCK -> {
                        crate.open(event.player, 1)
                    }

                    Action.LEFT_CLICK_BLOCK -> {
                        // Creative mode won't trigger the break event, so we have to do it here, we won't worry about no permission because that's checked in the break event
                        if (event.player.isSneaking && Perm.PLACE.has(event.player)) {
                            Crates.i.removeCrate(crate)
                            MixinMessage.get().msgOne(
                                event.player,
                                MConf.i.crateBroken.replace("{prefix}", MConf.i.prefix)
                                    .replace("{crate-type}", crate.crateId)
                            )
                            event.clickedBlock.type = Material.AIR
                            return
                        }
                        ChestPreviewGui(crate, Perm.GIVE.has(event.player)).open(event.player)
                    }

                    else -> Unit
                }
            }
        }
    }

    /**
     * Makes sure the player doesn't maintain any metadata
     * if they leave the server.
     *
     * @param event The event.
     */

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onLeave(event: PlayerQuitEvent) {
        event.player.removeMetadata("crate_placing", com.github.spook.crates.CratesPlugin.instance)
        event.player.removeMetadata("crate_hypebox", com.github.spook.crates.CratesPlugin.instance)
    }

    /**
     * Checks if a block is being placed on a neighboring block
     * containing a crate.
     *
     * @param block The block.
     * @return True if the block is near a crate, false otherwise.
     */

    private fun isNearCrate(block: Block): Boolean {
        return BlockFace.values().any { face ->
            Crates.i.crateAt(block.getRelative(face).location)?.let { true } ?: false
        }
    }

}