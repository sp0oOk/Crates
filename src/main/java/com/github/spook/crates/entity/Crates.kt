package com.github.spook.crates.entity

import com.github.spook.crates.gui.animations.CrateAnimation
import com.github.spook.crates.objects.LootTable
import com.github.spook.crates.objects.Reward
import com.massivecraft.massivecore.collections.MassiveMap
import com.massivecraft.massivecore.command.editor.annotation.EditorName
import com.massivecraft.massivecore.ps.PS
import com.massivecraft.massivecore.store.Entity
import com.massivecraft.massivecore.util.Txt
import eu.decentsoftware.holograms.api.DHAPI
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block

/**
 * Contains all the data stored on disk relating
 * to crates
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 * - 10/06/2023: spook - Added crate animation to default crate.
 */
@EditorName("config")
@Suppress("BooleanLiteralArgument")
class Crates : Entity<Crates>() {

    // ----------------------------------------------- //
    // FIELDS
    // ----------------------------------------------- //

    companion object {
        var i: Crates = Crates()

        @JvmStatic
        fun get(): Crates {
            return i
        }
    }

    @Transient
    private val randomRewards: MassiveMap<String, LootTable<Reward>> = MassiveMap()

    var crates: Set<Crate> = setOf(
        Crate(
            "common",
            CrateAnimation.MINECADIA,
            "&7Common",
            listOf(
                "&7Common Crate",
                "&fRight click to open!",
                "&fYou have &7%crates_keys_common% &fkey(s)"
            ),
            listOf(
                Reward(
                    "&cExample Prize",
                    listOf(
                        "&7This is an example prize!",
                        "&7You can add as many lines as you want!"
                    ),
                    listOf(
                        "give {player} diamond 1"
                    ),
                    10.0,
                    Material.DIAMOND,
                    0,
                    1,
                    false,
                    true
                )
            )
        )
    )

    /**
     * Override method that is used by MassiveCore to load the data from the database.
     *
     * @param that The data that is loaded from the database.
     */

    override fun load(that: Crates?): Crates {
        super.load(that)
        that?.crates?.forEach { handleLoad(it) }
        return this
    }

    /**
     * Loads rewards from Crate object into a LootTable.
     * that has weighted chances.
     *
     * @see LootTable
     * @param crate The crate object that contains the rewards.
     */

    private fun handleLoad(crate: Crate) {
        val rewards = LootTable<Reward>()
        crate.rewards.forEach { reward ->
            rewards.add(reward.chance, reward)
        }
        randomRewards[crate.crateId] = rewards
    }

    /**
     * Resynchronizes holograms with the database.
     *
     * NOTE: Any crates with a pre-existing hologram will not be affected.
     * I might end up making it so lines are compared and updated if they are different.
     * at a later date.
     */

    fun syncHolograms() {
        com.github.spook.crates.CratesPlugin.instance.newChain<Any>()
            .delay(2)
            .sync {
                crates.filter { it.location != null }.forEach { crate ->
                    val location = crate.location!!.asBukkitLocation()

                    DHAPI.getHologram("crates_${crate.crateId}")
                        ?.let {

                        }
                        ?: run {
                            val hologram = DHAPI.createHologram(
                                "crates_${crate.crateId}",
                                location
                                    .add(0.5, 1.5 + crate.hologramLore.size * (0.23 + 0.02), 0.5)
                            )

                            DHAPI.setHologramLines(hologram, crate.hologramLore.map { line ->
                                Txt.parse(line)
                            })
                        }
                }
            }
            .execute()
    }

    /**
     * Removes a placed crate from the database.
     * this does not delete the crate from the database, it just removes the location.
     *
     * @param crate The crate object to remove.
     */

    fun removeCrate(crate: Crate) {
        // crates -= crate
        val b: Block? = crate.location?.asBukkitBlock(true)

        b?.let {
            DHAPI.removeHologram("crates_${crate.crateId}")
        }
        crate.location = null
        //randomRewards.remove(crate.crateId)
        changed()
    }

    /**
     * Retrieves a Crate from the database based on the location.
     *
     * @param location The location of the crate.
     * @return The crate object if it exists, otherwise null.
     */

    fun crateAt(location: Location): Crate? {
        return crates.firstOrNull { it.location?.asBukkitLocation(true) == location }
    }

    fun removeAllHolograms() {
        crates.forEach { crate ->
            DHAPI.removeHologram("crates_${crate.crateId}")
        }
    }

    /**
     * Retrieves a Crate from the database based on the crateId (name).
     *
     * @param name The name of the crate.
     * @return The crate object if it exists, otherwise null.
     */

    fun crateByName(name: String): Crate? {
        return crates.firstOrNull { it.crateId == name }
    }

    /**
     * Retrieves a reward from the LootTable based on the crate name.
     *
     * @see LootTable
     * @see Reward
     * @param crateName The name of the crate.
     * @return The reward object if it exists, otherwise null.
     */

    fun getNextReward(crateName: String): Reward? {
        return randomRewards[crateName]?.random()
    }

    /**
     * Used to externally add crates to the database.
     * typically used by generators. {@see com.github.spook.crates.generators.Generators}
     *
     * @param crate The crate object to add.
     */

    fun addExternalCrate(crate: Crate) {
        if (crateByName(crate.crateId) != null) {
            com.github.spook.crates.CratesPlugin.instance.log("Could not add crate with id ${crate.crateId} as it already exists!")
            return
        }

        crates += crate
        handleLoad(crate)
        changed()
    }

    /**
     * Sets the location of a crate in the database.
     *
     * @param location The location to set.
     * @param crateId The id of the crate. (name)
     */

    fun setCrateLocation(location: Location, crateId: String) {
        val crate = crateByName(crateId)

        if (crate == null) {
            com.github.spook.crates.CratesPlugin.instance.log("Could not find crate with name $crateId when setting location!")
            return
        }

        crate.location = PS.valueOf(location)
        crate.changed()
    }


}