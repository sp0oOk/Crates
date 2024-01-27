package com.github.spook.crates.entity

import com.massivecraft.massivecore.store.SenderColl

/**
 * Coll for the [MPlayer] entity.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class MPlayerColl : SenderColl<MPlayer>() {

    companion object {
        val i: MPlayerColl = MPlayerColl()

        @JvmStatic
        fun get(): MPlayerColl {
            return i
        }
    }
}