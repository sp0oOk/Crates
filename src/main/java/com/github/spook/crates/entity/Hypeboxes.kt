package com.github.spook.crates.entity

import com.github.spook.crates.objects.LootTable
import com.github.spook.crates.objects.Reward
import com.massivecraft.massivecore.collections.MassiveMap
import com.massivecraft.massivecore.command.editor.annotation.EditorName
import com.massivecraft.massivecore.store.Entity
import org.bukkit.Material

@EditorName("config")
@Suppress("BooleanLiteralArgument")
class Hypeboxes : Entity<Hypeboxes>() {

    // ----------------------------------------------- //
    // FIELDS
    // ----------------------------------------------- //

    companion object {
        var i: Hypeboxes = Hypeboxes()

        @JvmStatic
        fun get(): Hypeboxes {
            return i
        }
    }

    @Transient
    private val randomRewards: MassiveMap<String, LootTable<Reward>> = MassiveMap()

    var hypeboxes: Set<Hypebox> = setOf(
        Hypebox(
            "drizzyDrake",
            Material.SKULL_ITEM,
            "&f&ki&6&l&nD&8&l&nR&6&l&nI&8&l&nZ&8&l&nY&r &6&l&nD&8&l&nR&6&l&nA&8&l&nK&6&l&nE&r &8&l&nH&6&l&nY&8&l&nP&6&l&nE&8&l&nB&6&l&nO&8&l&nX&f&ki",
            listOf(
                "&8Unlocked at &6&nstore.extinctionmc.com&r",
                "",
                "&6Want to let that &8dog &6out of you?",
                "&6Open the brand new {hypebox-display-name}",
                "&6to release all the dogs out!",
                "",
                "&7Click me to open and receive",
                "&7your rewards."
            ),
            listOf(
                Reward(
                    "Reward 1",
                    listOf(
                        "&cReward 1",
                        "&7This is a random reward.",
                        "&7You can edit this in the config."
                    ),
                    listOf(
                        "give {player} diamond 1"
                    ),
                    10.0,
                    Material.DIAMOND,
                    0,
                    1,
                    true,
                    true
                ),
                Reward(
                    "Reward 2",
                    listOf(
                        "&cReward 2",
                        "&7This is another random reward.",
                        "&7You can edit this in the config."
                    ),
                    listOf(
                        "give {player} emerald 1"
                    ),
                    10.0,
                    Material.EMERALD,
                    0,
                    1,
                    true,
                    true
                ),
                Reward(
                    "Reward 3",
                    listOf(
                        "&cReward 3",
                        "&7This is yet another random reward.",
                        "&7You can edit this in the config."
                    ),
                    listOf(
                        "give {player} iron_ingot 1"
                    ),
                    10.0,
                    Material.IRON_INGOT,
                    0,
                    1,
                    true,
                    true
                ),
                Reward(
                    "Reward 4",
                    listOf(
                        "&cReward 4",
                        "&7This is a unique random reward.",
                        "&7You can edit this in the config."
                    ),
                    listOf(
                        "give {player} gold_ingot 1"
                    ),
                    10.0,
                    Material.GOLD_INGOT,
                    0,
                    1,
                    true,
                    true
                ),
                Reward(
                    "Reward 5",
                    listOf(
                        "&cReward 5",
                        "&7This is a special random reward.",
                        "&7You can edit this in the config."
                    ),
                    listOf(
                        "give {player} emerald_block 1"
                    ),
                    10.0,
                    Material.EMERALD_BLOCK,
                    0,
                    1,
                    true,
                    true
                ),
                Reward(
                    "Reward 6",
                    listOf(
                        "&cReward 6",
                        "&7This is another unique random reward.",
                        "&7You can edit this in the config."
                    ),
                    listOf(
                        "give {player} redstone 1"
                    ),
                    10.0,
                    Material.REDSTONE,
                    0,
                    1,
                    true,
                    true
                ),
                Reward(
                    "Reward 7",
                    listOf(
                        "&cReward 7",
                        "&7This is a rare random reward.",
                        "&7You can edit this in the config."
                    ),
                    listOf(
                        "give {player} diamond_sword 1"
                    ),
                    10.0,
                    Material.DIAMOND_SWORD,
                    0,
                    1,
                    true,
                    true
                ),
                Reward(
                    "Reward 8",
                    listOf(
                        "&cReward 8",
                        "&7This is a valuable random reward.",
                        "&7You can edit this in the config."
                    ),
                    listOf(
                        "give {player} bedrock 1"
                    ),
                    10.0,
                    Material.BEDROCK,
                    0,
                    1,
                    true,
                    true
                ),
                Reward(
                    "Reward 9",
                    listOf(
                        "&cReward 9",
                        "&7This is a mysterious random reward.",
                        "&7You can edit this in the config."
                    ),
                    listOf(
                        "give {player} ender_pearl 1"
                    ),
                    10.0,
                    Material.ENDER_PEARL,
                    0,
                    1,
                    true,
                    true
                ),
                Reward(
                    "Reward 10",
                    listOf(
                        "&cReward 10",
                        "&7This is the final random reward.",
                        "&7You can edit this in the config."
                    ),
                    listOf(
                        "give {player} blaze_powder 1"
                    ),
                    10.0,
                    Material.BLAZE_POWDER,
                    0,
                    1,
                    true,
                    true
                )
            ),
            "40061"
        )
    )


    override fun load(that: Hypeboxes?): Hypeboxes {
        super.load(that)
        return this
    }

    fun forceLoadHypeboxes() {
        hypeboxes.forEach { handleLoad(it) }
    }

    private fun handleLoad(hypebox: Hypebox) {
        val rewards = LootTable<Reward>()
        hypebox.rewards.forEach { rewards.add(it.chance, it) }
        randomRewards[hypebox.hypeboxId] = rewards
    }

    fun getNextReward(crateName: String): Reward? {
        return randomRewards[crateName]?.random()
    }

    fun getById(id: String): Hypebox? {
        return hypeboxes.firstOrNull { it.hypeboxId == id }
    }


}