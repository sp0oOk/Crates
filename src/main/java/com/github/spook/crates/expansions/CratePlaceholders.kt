package com.github.spook.crates.expansions

import com.github.spook.crates.entity.MPlayer
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

/**
 * Placeholder expansion for ExtinctionCrates.
 *
 * Placeholders:
 * - %crates_keys_<crateName>% - The amount of keys a player has for a crate.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class CratePlaceholders : PlaceholderExpansion() {

    private val pattern = Regex("keys_(.*)")

    override fun getIdentifier(): String {
        return "crates"
    }

    override fun getAuthor(): String {
        return "spook"
    }

    override fun getVersion(): String {
        return "1.0.0"
    }

    override fun onPlaceholderRequest(player: Player?, params: String): String? {
        if (params.matches(pattern)) {
            val crateName = params.replace(pattern, "$1")
            return MPlayer.get(player!!)!!.getKeys(crateName).toString()
        }

        return null
    }
}