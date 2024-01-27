package com.github.spook.crates.integration.extinctiongifts

import com.massivecraft.massivecore.Integration

/**
 * The integration for ExtinctionGifts.
 *
 * @author spook
 *
 * Updates:
 * - 10/08/2023: spook - Initial creation.
 */
class IntegrationExtinctionGifts : Integration() {

    companion object {
        private val i = IntegrationExtinctionGifts()

        @JvmStatic
        fun get(): IntegrationExtinctionGifts {
            return i
        }
    }

    init {
        setPluginName("ExtinctionGifts")
    }

    override fun getEngine(): EngineExtinctionGifts {
        return EngineExtinctionGifts.get()
    }

}