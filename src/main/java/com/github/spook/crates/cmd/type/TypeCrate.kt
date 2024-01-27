package com.github.spook.crates.cmd.type

import com.github.spook.crates.entity.Crates
import com.massivecraft.massivecore.MassiveException
import com.massivecraft.massivecore.command.type.TypeAbstract
import org.bukkit.command.CommandSender
import kotlin.jvm.Throws

/**
 * This is a type that allows for tab completion of crate names.
 *
 * @see com.github.spook.crates.cmd.CmdCrates
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class TypeCrate : TypeAbstract<String>(String::class.java) {

    companion object {
        private val i = TypeCrate()
        fun get(): TypeCrate {
            return i
        }
    }

    @Throws(MassiveException::class)
    override fun read(p0: String?, p1: CommandSender?): String {
        val possibleName: String? = if (Crates.i.crates.any { crate -> crate.crateId.equals(p0, true) }) {
            p0
        } else {
            null
        }

        return possibleName
                ?: throw MassiveException().apply { addMsg("<b>No crate found matching \"<p>%s<b>\"<b>. Use tab-complete to autofill crate names.", p0) }
    }

    override fun getTabList(p0: CommandSender?, p1: String?): MutableCollection<String> {
        return Crates.i.crates.map { crate -> crate.crateId }.toMutableList()
    }

}