package com.github.spook.crates.generators.impl

import com.github.spook.crates.entity.Crate
import com.github.spook.crates.entity.Crates
import com.github.spook.crates.generators.AbstractGenerator
import com.github.spook.crates.gui.animations.CrateAnimation
import com.github.spook.crates.objects.Reward
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

/**
 * Generator for CloudCrates
 *
 * Note: This will read your CloudCrates configurations
 * and translate them into ExtinctionCrates crates
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 * - 10/06/2023: spook - Added CrateAnimation to generated crates.
 */
class CloudCrateGenerator : AbstractGenerator() {

    private val cratesFolder = File("plugins/CloudCrates/crates")
    private var currentReward = ""

    override fun run(): Boolean {

        // If no CloudCrates folder exists, skip generation
        if (!cratesFolder.exists()) {
            log("CloudCrates folder does not exist, skipping generation")
            return false
        }

        // Log header
        log(headerFormat)

        // Diagnostic variables
        var amount = 0
        val startMs = System.currentTimeMillis()

        // Walk files ending in .yml
        cratesFolder.walk().filter { it.isFile && it.extension == "yml" }.forEach { crateFile ->
            log("- Found crate file: ${crateFile.name}")
            create(crateFile)?.let {
                Crates.i.addExternalCrate(it)
                log("  - Created crate: ${it.crateId} with a total of ${it.rewards.size} rewards!")
                amount++
            } ?: run {
                log("  - Failed to create crate from file: ${crateFile.name}")
            }
        }

        val endMs = System.currentTimeMillis()
        log("Generated $amount crates in ${endMs - startMs}ms")
        log(footerFormat)

        return true
    }

    /**
     * Tries to create a {@link Crate} from a given CloudCrates Crate file
     *
     * @param ymlFile The file to create the crate from
     * @return The crate if successful, null otherwise
     */

    private fun create(ymlFile: File): Crate? {
        val c = YamlConfiguration.loadConfiguration(ymlFile)

        if (c.getString("Settings.Name") == null) {
            log("  - Failed to create crate from file: ${ymlFile.name} as it does not have a name!")
            return null
        }

        val crate = Crate(
            c.getString("Settings.Name").lowercase(Locale.getDefault()),
            CrateAnimation.NONE,
            c.getString("Virtual-Crate.Name"),
            c.getStringList("Hologram-Settings.Lines"),
            listOf()
        )

        c.getConfigurationSection("Rewards").getKeys(false)?.forEach { reward ->
            currentReward = reward
            val actualReward = Reward(
                c.item("Name"),
                c.item("Lore"),
                c.getStringList("Rewards.$reward.Commands"),
                c.getDouble("Rewards.$reward.Settings.Chance"),
                getMaterial(c.item<String>("Material").uppercase()),
                c.item("Durability"),
                c.item("Amount"),
                false,
                c.getBoolean("Rewards.$reward.Message-Settings.Broadcast-Message.Enabled")
            )
            crate.addReward(actualReward)
        }

        return crate
    }

    /**
     * Faster way to get a value from a YamlConfiguration
     *
     * @param subKey The key to get the value from
     * @return The value
     */

    private inline fun <reified T> YamlConfiguration.item(subKey: String): T {
        val value = this.get("Rewards.$currentReward.Display-Item.$subKey")
        if (value is T) return value

        if (subKey == "Durability") {
            log("Unable to parse durability from $currentReward, defaulting to 0")
            return 0 as T
        }

        throw IllegalArgumentException("Could not parse $subKey from $currentReward")
    }

    private fun getMaterial(raw: String): Material {
        return runCatching { Material.valueOf(raw.uppercase()) }
            .getOrElse {
                log("Unable to parse material from $currentReward, defaulting to DIRT (found: $raw)")
                Material.DIRT
            }
    }

}