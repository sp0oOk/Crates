package com.github.spook.crates.cmd.type

import com.github.spook.crates.entity.Hypeboxes
import com.massivecraft.massivecore.MassiveException
import com.massivecraft.massivecore.command.type.TypeAbstract
import org.bukkit.command.CommandSender
import kotlin.jvm.Throws

/**
 * This is a type that allows for tab completion of hypebox names.
 *
 * @see com.github.spook.crates.cmd.hypeboxes.CmdHypebox
 * @author spook
 *
 * Updates:
 * - 10/08/2023: spook - Initial creation.
 */
class TypeHypebox : TypeAbstract<String>(String::class.java) {

    companion object {
        private val i = TypeHypebox()
        fun get(): TypeHypebox {
            return i
        }
    }

    @Throws(MassiveException::class)
    override fun read(p0: String?, p1: CommandSender?): String {
        val possibleName: String? = if (Hypeboxes.i.hypeboxes.any { hypebox -> hypebox.hypeboxId.equals(p0, true) }) {
            p0
        } else {
            null
        }

        return possibleName
            ?: throw MassiveException().apply { addMsg("<b>No hypebox found matching \"<p>%s<b>\"<b>. Use tab-complete to autofill hypebox names.", p0) }
    }

    override fun getTabList(p0: CommandSender?, p1: String?): MutableCollection<String> {
        return Hypeboxes.i.hypeboxes.map { hypebox -> hypebox.hypeboxId }.toMutableList()
    }

}