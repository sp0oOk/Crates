package com.github.spook.crates.entity

import com.github.spook.crates.engine.EngineHypeboxes
import com.github.spook.crates.gui.animations.impl.HypeboxAnimationGui
import com.github.spook.crates.integration.headdatabase.IntegrationHeadDatabase
import com.github.spook.crates.objects.Reward
import com.massivecraft.massivecore.store.EntityInternal
import com.massivecraft.massivecore.util.Txt
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * Hypebox entity.
 * Represents a Hypebox used in game.
 *
 * @author spook
 *
 * Updates:
 * - 10/08/2023: spook - Initial creation.
 */
data class Hypebox(
    val hypeboxId: String,
    val material: Material,
    val displayName: String,
    val lore: List<String>,
    var rewards: List<Reward>,
    val skullOwner: String? = null,
) : EntityInternal<Hypebox>() {

    fun build(): ItemStack {

        val itemStack = IntegrationHeadDatabase.get().isIntegrationActive
            .let {
                if (it) IntegrationHeadDatabase.get().engine.toHeadStack(
                    ItemStack(material),
                    skullOwner!!
                ) else ItemStack(material)
            }

        val meta = itemStack.itemMeta
        meta.displayName = Txt.parse(displayName)
        meta.lore = Txt.parse(lore.map { it.replace("{hypebox-display-name}", displayName) })
        val pdc = meta.persistentDataContainer
        pdc.set(EngineHypeboxes.key, PersistentDataType.STRING, hypeboxId)

        itemStack.itemMeta = meta
        return itemStack
    }

    fun open(owner: Player) {
        HypeboxAnimationGui(this).start(owner)
    }

}