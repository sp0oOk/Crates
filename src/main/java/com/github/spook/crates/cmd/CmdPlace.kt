package com.github.spook.crates.cmd

import com.github.spook.crates.Perm
import com.github.spook.crates.cmd.type.TypeCrate
import com.github.spook.crates.entity.Crates
import com.github.spook.crates.entity.MConf
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer
import com.massivecraft.massivecore.mixin.MixinMessage
import com.massivecraft.massivecore.util.ItemBuilder
import org.bukkit.metadata.FixedMetadataValue

/**
 * This allows for administrators to place down crates.
 * turning them into a crate... :O
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class CmdPlace : CrateCommand() {

    init {
        aliases = listOf("place")
        addParameter(TypeCrate.get(), "crate")
        addRequirements<CmdPlace>(RequirementHasPerm.get(Perm.PLACE), RequirementIsPlayer.get())
    }

    override fun perform() {
        val crate = readArg<String>()

        Crates.i.crateByName(crate)?.let {
            me.setMetadata("crate_placing", FixedMetadataValue(com.github.spook.crates.CratesPlugin.instance, it.crateId))
            MixinMessage.get().msgOne(me,
                    MConf.i.placeCrateItem
                            .replace("{prefix}", MConf.i.prefix)
                            .replace("{crate-display-name}", it.displayName)
                            .replace("{crate-type}", MConf.i.crateType.name))
            me.inventory.addItem(ItemBuilder(MConf.i.crateType).displayname(it.crateId + " -- Crate")
                    .lore("Place Me Down To Register!")
                    .build())
        } ?: run {
            MixinMessage.get().msgOne(me,
                    MConf.i.crateNotFound
                            .replace("{prefix}", MConf.i.prefix)
                            .replace("{crate-name}", crate))
        }

    }

}