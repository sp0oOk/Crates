package com.github.spook.crates.integration.extinctiontoggles

import com.massivecraft.massivecore.Integration

/**
 * The integration for ExtinctionToggles.
 *
 * @author spook
 *
 * Updates:
 * - 10/05/2023: spook - Initial creation.
 */
class IntegrationExtinctionToggles : Integration() {

    companion object {
        private val i = IntegrationExtinctionToggles()

        @JvmStatic
        fun get(): IntegrationExtinctionToggles {
            return i
        }
    }

    init {
        setPluginName("ExtinctionToggles")
    }

    override fun getEngine(): EngineExtinctionToggles {
        return EngineExtinctionToggles.get()
    }

}