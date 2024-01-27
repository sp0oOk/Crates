package com.github.spook.crates.entity

import com.massivecraft.massivecore.store.Coll

/**
 * Coll for the [MConf] entity.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class MConfColl : Coll<MConf>() {

    companion object {
        @JvmStatic
        val i: MConfColl = MConfColl()

        @JvmStatic
        fun get(): MConfColl {
            return i
        }
    }

    override fun setActive(active: Boolean) {
        super.setActive(active)
        if (!active) {
            return
        }
        MConf.i = get("instance", true)
    }

}