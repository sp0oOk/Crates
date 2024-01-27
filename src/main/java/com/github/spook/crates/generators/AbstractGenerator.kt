package com.github.spook.crates.generators

/**
 * Abstract class for all generators,
 * holds all the common methods and variables.
 * All generators should extend this class.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
abstract class AbstractGenerator {

    internal val headerFormat: String = "=".repeat(5) + " [" + this.javaClass.simpleName + "] " + "=".repeat(5)
    internal val footerFormat: String = "=".repeat(14 + this.javaClass.simpleName.length)

    abstract fun run(): Boolean

    fun log(message: String, vararg replacement: String) {
        println("[ExtinctionCrates][Generator] $message".format(replacement))
    }
}