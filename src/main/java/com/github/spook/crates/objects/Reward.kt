package com.github.spook.crates.objects

import com.github.spook.crates.entity.MConf
import com.github.spook.crates.integration.extinctiontoggles.IntegrationExtinctionToggles
import com.extinctionmc.extinctiontoggles.entity.Setting
import com.massivecraft.massivecore.mixin.MixinMessage
import com.massivecraft.massivecore.store.EntityInternal
import com.massivecraft.massivecore.util.ItemBuilder
import com.massivecraft.massivecore.util.Txt
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

/**
 * Represents a reward that can be won from a crate.
 * All rewards are created by this class.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 * - 10/05/2023: spook - Added integration with ExtinctionToggles.
 */
data class Reward(
    val displayName: String,
    private val lore: List<String>,
    val commands: List<String>,
    val chance: Double,
    private val material: Material,
    private val data: Int,
    private val amount: Int,
    private val glow: Boolean,
    internal val announce: Boolean,
) : EntityInternal<Reward>() {


    /**
     * Converts this reward to an [ItemStack].
     *
     * @param admin Whether to include admin information.
     * @return The reward as an [ItemStack].
     */

    fun toItem(admin: Boolean = false): ItemStack {
        val cloned = lore.toMutableList()

        admin.takeIf { it }?.let {
            cloned.addAll(
                listOf(
                    " ",
                    "&9&l(!) &9Admin Information",
                    " &9&l* &fCommands: &7${commands.size}",
                    " &9&l* &fChance: &7$chance",
                    "",
                    "&7&nLeft-Click&7 to simulate winning this reward."
                )
            )
        }

        return ItemBuilder(material)
            .data(data.toByte())
            .amount(amount)
            .displayname(displayName)
            .lore(Txt.parse(cloned))
            .apply { if (glow) glow() }
            .build()
    }


    /**
     * Fired when a player wins this reward.
     *
     * @param player The player who won the reward.
     * @param crateName The name of the crate that was opened.
     */

    fun win(player: Player, crateName: String? = null, isHypebox: Boolean = false) {
        val server = player.server

        commands.forEach { command ->
            val replacedCommand =
                command.replace("%player%", player.name).replace("{player}", player.name)
            server.dispatchCommand(server.consoleSender, replacedCommand)
        }

        if (announce) {
            val extinctionToggles = IntegrationExtinctionToggles.get()
            val announceMessage = isHypebox
                .takeIf { it }
                ?.let { MConf.i.hypeboxWonReward }
                ?: MConf.i.announceMessage

            val prefix = MConf.i.prefix

            for (onlinePlayer in server.onlinePlayers) {
                if (extinctionToggles.isIntegrationActive && !extinctionToggles.engine.isToggled(
                        onlinePlayer,
                        Setting.CRATES
                    )
                ) {
                    continue
                }

                val replacedMessage = announceMessage
                    .replace("{player}", player.name)
                    .replace("%player%", player.name)
                    .replace("{player-display-name}", player.displayName)
                    .replace("{crate-display-name}", crateName ?: "a crate")
                    .replace("{reward-display-name}", displayName)
                    .replace("{prefix}", prefix)

                MixinMessage.get().msgOne(onlinePlayer, replacedMessage)
            }
        }
    }
}

/**
 * Extension function for [ItemBuilder] to set the data of an item.
 * by not having to use MaterialData.
 *
 * @param data The data to set.
 * @return The [ItemBuilder] with the data set.
 */

private fun ItemBuilder.data(data: Byte): ItemBuilder {
    return this.data(MaterialData(material, data))
}
