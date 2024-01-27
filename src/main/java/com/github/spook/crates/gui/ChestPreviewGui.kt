package com.github.spook.crates.gui

import com.github.spook.crates.Perm
import com.github.spook.crates.entity.Crate
import com.github.spook.crates.entity.MConf
import com.massivecraft.massivecore.chestgui.ChestAction
import com.massivecraft.massivecore.chestgui.PagedInventory
import com.massivecraft.massivecore.chestgui.`object`.PagedInventoryButton
import com.massivecraft.massivecore.util.ItemBuilder
import com.massivecraft.massivecore.util.Txt
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.stream.IntStream
import kotlin.streams.toList

/**
 * A GUI for previewing a crate.
 * This will be what player's see when they left-click a crate.
 * and will hold all the rewards that the crate has. in paginated form.
 *
 * @author spook
 * @param crate The crate to preview.
 * @param admin Whether the player is an admin.
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class ChestPreviewGui(crate: Crate, admin: Boolean = false) : PagedInventory(Bukkit.createInventory(null, 54, Txt.parse(
        MConf.i.cratePreviewTitle.replace("{crate-display-name}", crate.displayName)
)),
        null, calculatePages(crate), IntStream.range(0, 45).toList()) {

    init {

        isAutoclosing = false

        crate.rewards.forEach { reward ->
            addGuiItem(reward.toItem(admin), ChestAction { action ->
                if (action.isLeftClick && Perm.GIVE.has(action.whoClicked as Player)) {
                    reward.win(action.whoClicked as Player, crate.displayName)
                    com.github.spook.crates.CratesPlugin.instance.log("Executing ${reward.commands.size} commands from ${reward.displayName} on behalf of ${action.whoClicked.name}")
                }
                return@ChestAction true
            })
        }

        setNextPageButton(PagedInventoryButton(currentPage, 53, ItemBuilder(Material.ARROW).displayname(Txt.parse("&aNext Page --->")).lore("&7Click to go to the next page.").build()) {
            return@PagedInventoryButton true
        })

        setPreviousPageButton(PagedInventoryButton(currentPage, 45, ItemBuilder(Material.ARROW).displayname(Txt.parse("&c<--- Previous Page")).lore("&7Click to go to the previous page.").build()) {
            return@PagedInventoryButton true
        })


        IntStream.range(45, 54).forEach { i ->

            if (currentPage > 1 && i == 45) {
                return@forEach
            }

            if (i == 49) {
                inventory.setItem(i,
                        ItemBuilder(Material.PAPER)
                                .displayname("&9&l(!) Crate Information")
                                .lore("&fRewards: &7 ${crate.rewards.size}", "&fPage: &7 $currentPage / $maxPages", " ", "&7Buy more crates at &9&nstore.aquaticpvp.com")
                                .build())

                return@forEach
            }

            if (currentPage == calculatePages(crate) && i == 53) {
                return@forEach
            }

            inventory.setItem(i, ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).displayname(" ").build())
        }

        refresh()
    }


}

/**
 * Calculates the amount of pages a crate will have.
 *
 * @param crate The crate to calculate the pages for.
 */

private fun calculatePages(crate: Crate): Int {
    return (crate.rewards.size / 45)
}
