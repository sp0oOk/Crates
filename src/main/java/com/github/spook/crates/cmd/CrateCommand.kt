package com.github.spook.crates.cmd

import com.massivecraft.massivecore.command.MassiveCommand

/**
 * Extension of MassiveCommand that allows for setup.
 * to be always enabled. for all the classes that extend it.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
open class CrateCommand : MassiveCommand() {
    init {
        isSetupEnabled = true
    }
}