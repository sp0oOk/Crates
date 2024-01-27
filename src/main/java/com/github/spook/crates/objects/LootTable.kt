package com.github.spook.crates.objects

import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * LootTable class. Used to make weighted / random loot tables.
 * because random loot tables are fun.
 *
 * @author spook
 * @param L The type of the loot.
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class LootTable<L> {
    private val loot: NavigableMap<Double, L> = TreeMap()
    private var totalWeight = 0.0

    /**
     * Add a new loot item to the loot table.
     *
     * @param weight The weight("Chance") of the loot item.
     * @param result The loot item.
     */

    fun add(weight: Double, result: L) {
        if (weight <= 0.0) {
            return
        }
        totalWeight += weight
        loot[totalWeight] = result
    }

    /**
     * Get a random loot item from the loot table.
     *
     * @return A random loot item.
     */

    fun random(): L? {
        val value = ThreadLocalRandom.current().nextDouble() * totalWeight
        return loot.ceilingEntry(value)?.value
    }


}