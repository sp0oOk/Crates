package com.github.spook.crates.integration.headdatabase

import com.massivecraft.massivecore.Engine
import me.arcaniax.hdb.api.DatabaseLoadEvent
import me.arcaniax.hdb.api.HeadDatabaseAPI
import org.bukkit.event.EventHandler
import org.bukkit.inventory.ItemStack

/**
 * Engine for HeadDatabase.
 *
 * @author spook
 *
 * Updates:
 * - 10/09/2023: spook - Initial creation.
 */
class EngineHeadDatabase : Engine() {

    companion object {
        private val i = EngineHeadDatabase()

        @JvmStatic
        fun get(): EngineHeadDatabase {
            return i
        }
    }

    private var api: HeadDatabaseAPI? = null

    // Not always called!
    @EventHandler
    fun onLoad(event: DatabaseLoadEvent) {
        api = HeadDatabaseAPI()
        com.github.spook.crates.CratesPlugin.instance.log("HeadDatabase database loaded, enabling HeadDatabase integration")
    }

    /**
     * Converts an ItemStack to a HeadDatabase head.
     *
     * @param itemStack The ItemStack to convert.
     * @param id The HeadDatabase ID to convert to.
     * @return The converted ItemStack or the original if HeadDatabase is not installed.
     */

    fun toHeadStack(itemStack: ItemStack, id: String): ItemStack {
        val actualApi = api ?: run {
            api = HeadDatabaseAPI()
            api
        }

        return actualApi?.getItemHead(id) ?: itemStack
    }


}