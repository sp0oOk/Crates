package com.github.spook.crates.integration.extinctiontoggles

import com.extinctionmc.extinctiontoggles.entity.Setting
import com.massivecraft.massivecore.Engine
import org.bukkit.entity.Player

/**
 * The engine for ExtinctionToggles integration.
 *
 * @author spook
 *
 * Updates:
 * - 10/05/2023: spook - Initial creation.
 */
class EngineExtinctionToggles : Engine() {

    companion object {
        private val i = EngineExtinctionToggles()

        @JvmStatic
        fun get(): EngineExtinctionToggles {
            return i
        }
    }

    fun isToggled(player: Player, setting: Setting): Boolean {
        return setting.isToggled(player)
    }

    fun toggle(player: Player, setting: Setting): Boolean {
        return setting.toggle(player)
    }

}