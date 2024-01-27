package com.github.spook.crates.cmd.hypeboxes

import com.github.spook.crates.Perm
import com.github.spook.crates.cmd.CrateCommand
import com.github.spook.crates.cmd.type.TypeHypebox
import com.github.spook.crates.entity.Hypeboxes
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm
import com.massivecraft.massivecore.command.type.sender.TypePlayer

/**
 * This is the command that gives hypeboxes to players.
 * OBVIOUSLY... this is a very dangerous command. :O
 *
 * @author spook
 *
 * Updates:
 * - 10/08/2023: spook - Initial creation.
 */
class CmdHypeboxGive : CrateCommand() {

    init {
        aliases = listOf("give")
        addParameter(TypeHypebox.get(), "hypebox")
        addParameter(TypePlayer.get(), "player", "you")
        setDesc("Gives a player a hypebox.")
        addRequirements<CmdHypeboxGive>(RequirementHasPerm.get(Perm.GIVE))
    }

    override fun perform() {
        val hypebox = readArg<String>()
        val player = readArg(me)

        Hypeboxes.i.getById(hypebox)?.let {
            player.inventory.addItem(it.build())
            msg("<g>You have given <h>${player.name} <g>a <h>${it.displayName}<g>.")
        }?: msg("<b>That hypebox does not exist in the config.")
    }

}