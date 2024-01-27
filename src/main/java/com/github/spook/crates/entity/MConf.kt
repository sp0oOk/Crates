package com.github.spook.crates.entity

import com.massivecraft.massivecore.store.Entity
import org.bukkit.ChatColor
import org.bukkit.Material

/**
 * Contains all the messages and settings
 * that drive the plugin.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 * - 10/06/2023: spook - Added forfeitedAnimation message. (Removed crateDoesNotHaveLocation)
 */
class MConf : Entity<MConf>() {

    companion object {
        @JvmStatic
        @Transient
        var i: MConf = MConf()

        @JvmStatic
        fun get(): MConf {
            return i
        }
    }

    // -- Settings -- //

    var aliases: MutableList<String> = mutableListOf("crate", "crates")
    var crateType: Material = Material.ENDER_CHEST
    var cratePreviewTitle: String = "&7Previewing Crate: &f{crate-display-name}";
    var hypeboxPreviewTitle: String = "%luckperms_prefix% %player_name%"

    // -- Messages -- //

    var prefix: String = "&9&lCrates &8➸&r"
    var cannotPlaceNearCrate: String = "{prefix} &cYou cannot place a crate near another crate."
    var invalidCrateBlock: String = "{prefix} &cYou cannot place the block {block-placed} as a crate (REQ: {crate-block})."
    var cratePlaced: String = "{prefix} &aYou have placed a crate of type {crate-type}."
    var notEnoughKeys: String = "{prefix} &cYou do not have enough keys to open a {crate-display-name}&c crate."
    var announceMessage: String = "{prefix} &f{player-display-name}&7 has opened a {crate-display-name}&7 crate and won {reward-display-name}&7."
    var notifyCratesGiven: String = "{prefix} &ayou &7have been given &f{amount}x {crate-display-name}&7 key(s)."
    var notifyCratesGave: String = "{prefix} &7you have given &f{amount}x {crate-display-name}&7 key(s) to &f{player-display-name}&7."
    var placeCrateItem: String = "{prefix} &cThe next &e{crate-type} &cyou place will be registered as a &f{crate-display-name}&c crate."
    var crateNotFound: String = "{prefix} &cThe crate &f{crate-name}&c does not exist."
    var cannotBreakCrate: String = "{prefix} &cYou do not have permission to break a crate."
    var crateBroken: String = "{prefix} &aYou have broken a crate of type {crate-type}."
    var giveAll: String = "{prefix} &aYou have given {player-size} player(s) {amount}x {crate-display-name}&a key(s)."
    var giveAllNotify: String = "{prefix} &aYou have been given {amount}x {crate-display-name}&a key(s)."
    var notEnoughSpace: String = "{prefix} &cYou must have at-least 3 inventory slots free to open a crate."
    var forfeitedAnimation: String = "{prefix} &7You have forfeited your crate opening, generating a random reward."

    var hypeboxJsonSelector: String = "[CLICK TO WATCH]"
    var hypeboxSelectorColor: ChatColor = ChatColor.GREEN
    var hypeboxJsonTooltip: String = "&aClick to watch {player-name} open this Hype Box."
    var hypeboxAnnouncement: String = "&c&lH&e&lY&a&lP&b&lE&d&lB&c&lO&e&lX &8➸ {player-formatted-display} &r &fis opening a {crate-display-name} [CLICK TO WATCH]&r"
    var hypeboxWonReward: String = "&c&lH&e&lY&a&lP&b&lE&d&lB&c&lO&e&lX &8➸ &c{player-display-name} &fhas won a &c{reward-display-name}&r from a {crate-display-name}&r"
}