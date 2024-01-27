package com.github.spook.crates.gui.animations

import com.github.spook.crates.entity.Crate
import com.github.spook.crates.gui.animations.impl.CadiaAnimationGui
import com.github.spook.crates.gui.animations.impl.HypeboxAnimationGui
import com.massivecraft.massivecore.chestgui.RefreshableChestGui
import org.bukkit.entity.Player

/**
 * An interface that allows for the animation of crates.
 *
 * @author spook
 *
 * Updates:
 * - 10/06/2023: spook - Initial creation.
 */
enum class CrateAnimation(private val animationKlass: Class<out RefreshableChestGui>?) {
    NONE(null),
    MINECADIA(CadiaAnimationGui::class.java);
    //HYPEBOX(HypeboxAnimationGui::class.java);

    /**
     * Starts the animation for a player.
     *
     * @param crate The crate to animate.
     * @param player The player to animate the crate for.
     * @throws IllegalArgumentException If the animation does not implement [Animated].
     */

    fun animate(crate: Crate, player: Player) {
        val animation = animationKlass!!.getConstructor(Crate::class.java).newInstance(crate)
        if (animation !is Animated) throw IllegalArgumentException("Animation ${animationKlass.name} does not implement Animated!")
        animation.start(player)
    }

}