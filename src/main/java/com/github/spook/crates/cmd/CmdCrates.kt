package com.github.spook.crates.cmd

import com.github.spook.crates.Perm
import com.github.spook.crates.cmd.hypeboxes.CmdWatch
import com.github.spook.crates.entity.MConf
import com.massivecraft.massivecore.command.MassiveCommandVersion
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm

/**
 * This is the base command for all crate commands.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 * - 10/05/2023: spook - Removed duplicate alias (version) as it's added by default.
 * - 10/08/2023: spook - Removed cmdTest and added cmdHypebox.
 */
@Suppress("unused")
class CmdCrates : CrateCommand() {

    private val cmdCratesGive: CmdGive = CmdGive()
    private val cmdCratesPlace: CmdPlace = CmdPlace()
    private val cmdCratesGenerate: CmdGenerate = CmdGenerate()
    private val cmdCratesGiveAll: CmdGiveAll = CmdGiveAll()
    private val cmdCratesWatch: CmdWatch = CmdWatch()

    private val cmdCratesVersion: MassiveCommandVersion =
        MassiveCommandVersion(com.github.spook.crates.CratesPlugin.instance)
            .addAliases<MassiveCommandVersion>("v")
            .addRequirements(RequirementHasPerm.get(Perm.VERSION))

    companion object {
        private val i = CmdCrates()

        @JvmStatic
        fun get(): CmdCrates {
            return i
        }
    }

    init {
        addRequirements<CmdCrates>(RequirementHasPerm.get(Perm.BASECOMMAND))
    }

    override fun getAliases(): MutableList<String> {
        return MConf.i.aliases
    }

}