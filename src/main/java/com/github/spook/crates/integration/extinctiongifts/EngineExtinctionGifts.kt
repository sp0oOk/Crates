package com.github.spook.crates.integration.extinctiongifts

import com.github.spook.crates.entity.Hypebox
import com.extinctionmc.extinctiongifts.entity.MPlayer
import com.extinctionmc.extinctiongifts.obj.Gift
import com.extinctionmc.extinctiontoggles.entity.Setting
import com.massivecraft.massivecore.Engine
import org.bukkit.entity.Player
import java.util.UUID

/**
 * The engine for ExtinctionGifts integration.
 *
 * @author spook
 *
 * Updates:
 * - 10/08/2023: spook - Initial creation.
 */
class EngineExtinctionGifts : Engine() {

    companion object {
        private val i = EngineExtinctionGifts()

        @JvmStatic
        fun get(): EngineExtinctionGifts {
            return i
        }
    }

    fun gift(player: Player, hypebox: Hypebox) {
        val mPlayer = MPlayer.get(player)
        mPlayer.addGift(
            Gift(
                UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670"),
                hypebox.displayName,
                hypebox.build()
            )
        )
    }

}