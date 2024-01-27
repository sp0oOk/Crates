package com.github.spook.crates.cmd.hypeboxes

import com.github.spook.crates.Perm
import com.massivecraft.massivecore.command.MassiveCommand
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm

/**
 * This is the base command for all crate commands.
 *
 * @author spook
 *
 * Updates:
 * - 10/08/2023: spook - Initial creation.
 */
@Suppress("unused")
class CmdHypebox : MassiveCommand() {

    private val cmdHypeboxGive: CmdHypeboxGive = CmdHypeboxGive()

    companion object {
        private val i = CmdHypebox()

        @JvmStatic
        fun get(): CmdHypebox {
            return i
        }
    }

    init {
        addChild<CmdHypeboxGive>(cmdHypeboxGive)
        addRequirements<CmdHypebox>(RequirementHasPerm.get(Perm.BASECOMMAND))
    }

    override fun getAliases(): List<String> {
        return listOf("hypebox", "hypeboxes")
    }

}