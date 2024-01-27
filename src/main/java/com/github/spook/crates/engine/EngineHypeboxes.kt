package com.github.spook.crates.engine

import com.github.spook.crates.entity.Hypeboxes
import com.massivecraft.massivecore.Engine
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class EngineHypeboxes : Engine() {

    companion object {
        private val i = EngineHypeboxes()
        internal val key: NamespacedKey = NamespacedKey(com.github.spook.crates.CratesPlugin.instance, "hypebox")

        @JvmStatic
        fun get(): EngineHypeboxes {
            return i
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.action.name.contains("RIGHT")) {
            if (event.player.inventory.itemInHand.itemMeta?.persistentDataContainer?.has(
                    key,
                    PersistentDataType.STRING
                ) == true
            ) {
                event.isCancelled = true

                if (event.player.hasMetadata("crate_hypebox")) {
                    return;
                }

                Hypeboxes.i.getById(
                    event.player.inventory.itemInHand.itemMeta?.persistentDataContainer?.get(
                        key,
                        PersistentDataType.STRING
                    )!!
                )?.let {

                    if (it.rewards.size < 9) {
                        return;
                    }

                    it.open(event.player)

                    if (event.player.inventory.itemInHand.amount == 1) {
                        event.player.inventory.itemInHand = null
                    } else {
                        event.player.inventory.itemInHand.amount -= 1
                    }
                }
            }
        }
    }


}