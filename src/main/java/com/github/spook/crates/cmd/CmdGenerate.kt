package com.github.spook.crates.cmd

import com.github.spook.crates.Perm
import com.github.spook.crates.cmd.type.TypeGenerator
import com.github.spook.crates.generators.Generators
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer

/**
 * This is the command that generates crates.
 * via the generator system.
 *
 * @see com.github.spook.crates.generators.Generators
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class CmdGenerate : CrateCommand() {

    init {
        aliases = listOf("generate")
        addParameter(TypeGenerator.instance, "generator")
        addRequirements<CmdGenerate>(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.GENERATE))
    }

    override fun perform() {
        val generator = readArg<Generators>()

        generator.generate().takeIf { it }
                ?.run {
                    msg("<g>Generating crates with <h>${generator.name} <g>generator.")
                }
                ?: msg("<b>Failed to generate crates with <h>${generator.name} <b>generator.")
    }

}