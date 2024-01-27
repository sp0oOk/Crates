package com.github.spook.crates.cmd.hypeboxes

import com.github.spook.crates.Perm
import com.github.spook.crates.cmd.CrateCommand
import com.github.spook.crates.gui.animations.impl.HypeboxAnimationGui
import com.massivecraft.massivecore.MassiveException
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer
import com.massivecraft.massivecore.command.type.sender.TypePlayer
import org.bukkit.entity.Player
import kotlin.jvm.Throws

/**
 * This is the command that allows players to watch other players open crates.
 * specifically hypeboxes.
 *
 * @author spook
 *
 * Updates:
 * - 10/07/2023: spook - Initial creation.
 * - 10/09/2023: spook - Recoded this to be cleaner.
 */
class CmdWatch : CrateCommand() {

    init {
        aliases = listOf("watch")
        addParameter(TypePlayer.get(), "player")
        addRequirements<CmdWatch>(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.WATCH))
    }

    @Throws(MassiveException::class)
    override fun perform() {
        val player = readArg<Player>()

        player.takeIf { it != me }?.apply {
            if (!hasMetadata("crate_hypebox")) {
                throw MassiveException().apply { addMsg("<b>That $name is not opening a hypebox!") }
            }

            val hypebox = getMetadata("crate_hypebox").first().value() as HypeboxAnimationGui
            hypebox.start(me)
        }
            ?: throw MassiveException().apply { addMsg("<b>You cannot watch yourself open a hypebox!") }
    }

}