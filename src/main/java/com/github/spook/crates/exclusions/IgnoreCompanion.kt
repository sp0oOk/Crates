package com.github.spook.crates.exclusions

import com.massivecraft.massivecore.xlib.gson.ExclusionStrategy
import com.massivecraft.massivecore.xlib.gson.FieldAttributes

/**
 * Excludes the Companion object from serialization.
 * This will stop gson from trying to serialize the Companion object
 * thus, making it so this plugin actually works.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class IgnoreCompanion : ExclusionStrategy {

    override fun shouldSkipField(p0: FieldAttributes?): Boolean {
        if (p0 != null) {
            return p0.name?.equals("Companion") ?: false
        }

        return false
    }

    override fun shouldSkipClass(p0: Class<*>?): Boolean {
        return false
    }

}