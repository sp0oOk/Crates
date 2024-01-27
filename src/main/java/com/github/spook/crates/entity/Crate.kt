package com.github.spook.crates.entity

import com.github.spook.crates.gui.animations.CrateAnimation
import com.github.spook.crates.objects.Reward
import com.massivecraft.massivecore.mixin.MixinMessage
import com.massivecraft.massivecore.ps.PS
import com.massivecraft.massivecore.store.EntityInternal
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * Contains all the standard data that all
 * crates have in common with each other.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 * - 10/06/2023: spook - Added crateAnimation field.
 */
data class Crate(
    val crateId: String,
    val crateAnimation: CrateAnimation = CrateAnimation.NONE,
    val displayName: String,
    internal val hologramLore: List<String>,
    var rewards: List<Reward>,
) : EntityInternal<Crate>() {

    internal var location: PS? = null


    /**
     * Opens X amount of crates for a player.
     *
     * @param player The player to open the crate for.
     * @param amount The amount of crates to open.
     */

    fun open(player: Player, amount: Int) {
        val mPlayer = MPlayerColl.i.get(player)
        if (mPlayer.getKeys(crateId) < amount) {
            MixinMessage.get().msgOne(
                player, MConf.i.notEnoughKeys.replace(
                    "{crate-display-name}", displayName
                ).replace(
                    "{prefix}", MConf.i.prefix
                )
            )
            return
        }

        if (!hasAvailableSpace(player)) {
            MixinMessage.get().msgOne(
                player, MConf.i.notEnoughSpace
                    .replace("{prefix}", MConf.i.prefix)
            )
            return
        }

        mPlayer.takeKeys(this, amount)

        if (crateAnimation == CrateAnimation.NONE) {
            Crates.i.getNextReward(crateId)?.win(player, displayName)
            return
        }

        crateAnimation.animate(this, player)
    }

    private fun hasAvailableSpace(player: Player, slots: Int = 3): Boolean {
        var available = 0

        for (itemStack in player.inventory.contents) {
            if (itemStack == null || itemStack.type == Material.AIR) {
                available++
            }
        }

        return available >= slots
    }

    /**
     * Adds a reward to the crate.
     *
     * @param reward The reward to add.
     */

    fun addReward(reward: Reward) {
        rewards += reward
    }


}