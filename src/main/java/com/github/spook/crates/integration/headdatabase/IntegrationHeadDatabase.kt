package com.github.spook.crates.integration.headdatabase

import com.massivecraft.massivecore.Integration

/**
 * Integration for HeadDatabase.
 *
 * @author spook
 *
 * Updates:
 * - 10/09/2023: spook - Initial creation.
 */
class IntegrationHeadDatabase : Integration() {

    companion object {
        private val i = IntegrationHeadDatabase()

        @JvmStatic
        fun get(): IntegrationHeadDatabase {
            return i
        }
    }

    init {
        setPluginName("HeadDatabase")
    }

    override fun getEngine(): EngineHeadDatabase {
        return EngineHeadDatabase.get()
    }

}