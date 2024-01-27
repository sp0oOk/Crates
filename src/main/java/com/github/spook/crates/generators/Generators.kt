package com.github.spook.crates.generators

import com.github.spook.crates.generators.impl.CloudCrateGenerator

/**
 * Enum for the different generators.
 * This enum is used to run the generators.
 * Each generator should be added here.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
enum class Generators(private val generator: AbstractGenerator) {
    CLOUD_CRATES(CloudCrateGenerator());

    fun generate(): Boolean {
        return generator.run()
    }
}