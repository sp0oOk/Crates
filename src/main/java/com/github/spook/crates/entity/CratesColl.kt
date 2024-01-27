package com.github.spook.crates.entity

import com.massivecraft.massivecore.store.Coll

/**
 * Coll for the [Crates] entity.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class CratesColl : Coll<Crates>() {

    companion object {
        private val i = CratesColl()

        @JvmStatic
        fun get(): CratesColl {
            return i
        }
    }

    override fun setActive(active: Boolean) {
        super.setActive(active)
        if (!active) {
            return
        }

        Crates.i = get("instance", true)
    }

}