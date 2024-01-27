package com.github.spook.crates.gui.animations

import org.bukkit.entity.Player

/**
 * Represents an animation that can be played
 * when a player opens a crate.
 *
 * @author spook
 *
 * Updates:
 * - 10/06/2023: spook - Initial creation.
 */
interface Animated {
    fun start(player: Player)
    fun end(endReason: EndReason)
}