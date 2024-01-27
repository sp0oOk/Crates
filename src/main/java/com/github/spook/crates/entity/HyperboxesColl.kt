package com.github.spook.crates.entity

import com.massivecraft.massivecore.store.Coll

/**
 * Coll for the [Hypeboxes] entity.
 *
 * @author spook
 *
 * Updates:
 * - 10/08/2023: spook - Initial creation.
 */
class HyperboxesColl : Coll<Hypeboxes>() {

    companion object {
        private val i = HyperboxesColl()

        @JvmStatic
        fun get(): HyperboxesColl {
            return i
        }
    }

    override fun setActive(active: Boolean) {
        super.setActive(active)
        if (!active) {
            return
        }

        Hypeboxes.i = get("instance", true)
    }

}