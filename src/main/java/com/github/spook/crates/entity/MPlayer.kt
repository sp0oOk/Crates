package com.github.spook.crates.entity

import com.massivecraft.massivecore.collections.MassiveMap
import com.massivecraft.massivecore.store.SenderEntity

/**
 * Contains all the data for a player.
 * represents a CratePlayer, holds players keys,
 * that's pretty much it.
 *
 * @author spook
 * @see CratePlayer
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class MPlayer : SenderEntity<MPlayer>(), CratePlayer {

    private var keys: MassiveMap<String, Int> = MassiveMap()

    companion object {
        fun get(oid: Any): MPlayer? {
            return MPlayerColl.i.get(oid)
        }
    }

    /**
     * Loads the data from the [that] object.
     *
     * @param that The object to load from.
     * @return The loaded object.
     */

    override fun load(that: MPlayer?): MPlayer {
        setAllKeys(that!!.keys)

        // Load Uninitialized Keys at a defaulted value of 0
        Crates.i.crates.forEach { crate ->
            if (!keys.containsKey(crate.crateId)) {
                keys[crate.crateId] = 0
            }
        }



        return this
    }

    /**
     * Overwrites all player's keys with the [cratesData].
     *
     * @param cratesData The data to overwrite with.
     */

    private fun setAllKeys(cratesData: MassiveMap<String, Int>) {
        keys = cratesData
    }

    /**
     * Retrieve the amount of keys a player has for a crate
     * by the crates name. (id)
     *
     * @param crateName The name of the crate to get the keys for. (id)
     */

    fun getKeys(crateName: String): Int {
        return keys[crateName] ?: 0
    }

    /**
     * Adds keys to a specific crate for a player.
     *
     * @param crateName The name of the crate to add keys to. (id)
     * @param amount The amount of keys to add.
     */

    fun addKeys(crateName: String, amount: Int) {
        keys[crateName] = getKeys(crateName) + amount
        changed()
    }

    /**
     * Takes keys from a specific crate for a player.
     *
     * @param crate The crate to take keys from.
     * @param amount The amount of keys to take.
     */
    
    fun takeKeys(crate: Crate, amount: Int) {
        val amountDeducted = getKeys(crate.crateId) - amount
        keys[crate.crateId] = if (amount < 0) 0 else amountDeducted
        changed()
    }


}