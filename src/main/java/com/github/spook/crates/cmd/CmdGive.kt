package com.github.spook.crates.cmd

import com.github.spook.crates.Perm
import com.github.spook.crates.cmd.type.TypeCrate
import com.github.spook.crates.cmd.type.TypeMPlayer
import com.github.spook.crates.entity.Crates
import com.github.spook.crates.entity.MConf
import com.github.spook.crates.entity.MPlayer
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm
import com.massivecraft.massivecore.command.type.primitive.TypeInteger
import com.massivecraft.massivecore.mixin.MixinMessage

/**
 * This is the command that gives crate keys to players.
 * OBVIOUSLY... this is a very dangerous command. :O
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class CmdGive : CrateCommand() {

    init {
        aliases = listOf("give")
        addParameter(TypeMPlayer.get(), "player")
        addParameter(TypeCrate.get(), "crate")
        addParameter(TypeInteger.get(), "amount", "1")
        addRequirements<CmdGive>(RequirementHasPerm.get(Perm.GIVE))
    }

    override fun perform() {
        val player = readArg<MPlayer>()
        val crate = readArg<String>()
        val amount = readArg(1)

        Crates.i.crateByName(crate)?.let {
            player!!.addKeys(it.crateId, amount)

            val notifyCratesGiven = MConf
                    .i
                    .notifyCratesGiven
                    .replace("{prefix}", MConf.i.prefix)
                    .replace("{amount}", amount.toString())
                    .replace("{crate-display-name}", it.displayName)

            val notifyCratesGave = MConf
                    .i
                    .notifyCratesGave
                    .replace("{prefix}", MConf.i.prefix)
                    .replace("{amount}", amount.toString())
                    .replace("{crate-display-name}", it.displayName)
                    .replace("{player-display-name}", player.player.displayName)

            MixinMessage.get().msgOne(me, notifyCratesGiven)

            if (!player.isOffline) {
                MixinMessage.get().msgOne(player, notifyCratesGave)
               // it.location.run {
                    //val crateDoesNotHaveLocation = MConf
                    //      .i
                    //    .crateDoesNotHaveLocation
                    //  .replace("{prefix}", MConf.i.prefix)
                    //.replace("{crate-display-name}", it.displayName)

                    // MixinMessage.get().msgOne(me, crateDoesNotHaveLocation)
              //  }
            }

        }


    }

}