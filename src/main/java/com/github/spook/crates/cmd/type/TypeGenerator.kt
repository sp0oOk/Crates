package com.github.spook.crates.cmd.type

import com.github.spook.crates.generators.Generators
import com.massivecraft.massivecore.MassiveException
import com.massivecraft.massivecore.command.type.enumeration.TypeEnum
import org.bukkit.command.CommandSender

/**
 * This is a type that allows for tab completion of generator names.
 * from the Generators enum.
 *
 * @see com.github.spook.crates.generators.Generators
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class TypeGenerator private constructor() : TypeEnum<Generators>(Generators::class.java) {

    companion object {
        val instance = TypeGenerator()
    }

    override fun read(arg: String?, sender: CommandSender?): Generators {
        return Generators.values().firstOrNull() { it.name.equals(arg, ignoreCase = true) }
                ?: throw MassiveException().addMessage("<b>Invalid generator \"<p%s<p>\" <b>Tab complete for valid generators.")
    }

    override fun getTabList(sender: CommandSender?, arg: String?): MutableCollection<String> {
        return Generators.values().map { it.name }.toMutableList()
    }

}