package com.github.spook.crates

import com.github.spook.crates.cmd.CmdCrates
import com.github.spook.crates.cmd.hypeboxes.CmdHypebox
import com.github.spook.crates.engine.EngineCrates
import com.github.spook.crates.engine.EngineHypeboxes
import com.github.spook.crates.entity.*
import com.github.spook.crates.exclusions.IgnoreCompanion
import com.github.spook.crates.expansions.CratePlaceholders
import com.github.spook.crates.integration.extinctiongifts.IntegrationExtinctionGifts
import com.github.spook.crates.integration.extinctiontoggles.IntegrationExtinctionToggles
import com.github.spook.crates.integration.headdatabase.IntegrationHeadDatabase
import com.massivecraft.massivecore.MassivePlugin
import com.massivecraft.massivecore.taskchain.BukkitTaskChainFactory
import com.massivecraft.massivecore.taskchain.TaskChain
import com.massivecraft.massivecore.taskchain.TaskChainFactory
import com.massivecraft.massivecore.util.Txt

/**
 * Main class for ExtinctionCrates.
 * Everything in the world has a beginning, and this is ours. :)
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 * - 10/08/2023: spook - Added Hypebox classes to be activated.
 * - 10/08/2023: spook - Added ExtinctionGifts integration.
 * - 10/09/2023: spook - Added HeadDatabase integration.
 * - 10/09/2023: spook - Recoded placeholders() method to be cleaner.
 */
class CratesPlugin : MassivePlugin() {

    // ----------------------------------------------- //
    // FIELDS
    // ----------------------------------------------- //

    lateinit var taskChainFactory: TaskChainFactory
    private var placeholders: CratePlaceholders? = null

    companion object {
        lateinit var instance: CratesPlugin
            private set
    }

    init {
        instance = this
        versionSynchronized = false
    }

    /**
     * Called second, well first since we aren't overriding onEnablePre.
     */

    override fun onEnableInner() {
        taskChainFactory = BukkitTaskChainFactory.create(this)

        // If we don't ignore Companion fields, Gson will throw an exception
        val builder = super.getGsonBuilder()
        builder.setExclusionStrategies(IgnoreCompanion())
        super.setGson(builder.create())

        activate(
            // -- Entity Collections -- //
            MConfColl::class.java,
            CratesColl::class.java,
            HyperboxesColl::class.java,
            MPlayerColl::class.java,

            // -- Engine Classes -- //
            EngineCrates::class.java,
            EngineHypeboxes::class.java,

            // -- Command Classes -- //
            CmdCrates::class.java,
            CmdHypebox::class.java,

            // -- Integration Classes -- //
            IntegrationExtinctionToggles::class.java,
            IntegrationExtinctionGifts::class.java,
            IntegrationHeadDatabase::class.java
        )

        newChain<Any>()
            .delay(20)
            .sync {
                Crates.i.crates.forEach { crate ->
                    crate.location?.asBukkitBlock(true)?.takeIf { it.type != MConf.i.crateType }
                        ?.apply {
                            type = MConf.i.crateType
                        }
                }
            }
            .execute()

        placeholders()
        Hypeboxes.i.forceLoadHypeboxes()
        Crates.i.syncHolograms()
    }

    /**
     * Called when the plugin is disabled.
     */

    override fun onDisable() {
        super.onDisable()
        Crates.i.removeAllHolograms()
        placeholders?.unregister()
    }

    /**
     * Creates a new TaskChain.
     *
     * @return The new TaskChain.
     */

    inline fun <reified T> newChain(): TaskChain<T> {
        return taskChainFactory.newChain()
    }

    /**
     * Loads the placeholders if they haven't been loaded yet.
     *
     * @return The CratePlaceholders instance.
     */
    private fun placeholders(): CratePlaceholders {
        return placeholders ?: run {
            val newPlaceholders = CratePlaceholders()
            newPlaceholders.register()
            placeholders = newPlaceholders
            newPlaceholders
        }
    }

    fun parse(str: String, vararg args: String): String {
        if (args.size % 2 != 0) {
            throw IllegalArgumentException("Arguments must be divisible by 2.")
        }

        var parsed = str
        for (i in args.indices step 2) {
            parsed = parsed.replace(args[i], args[i + 1])
        }

        return Txt.parse(parsed)
    }

}
