package com.github.spook.crates.cmd

import com.github.spook.crates.Perm
import com.github.spook.crates.cmd.type.TypeCrate
import com.github.spook.crates.entity.Crates
import com.github.spook.crates.entity.MConf
import com.github.spook.crates.entity.MPlayer
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanTrue
import com.massivecraft.massivecore.command.type.primitive.TypeInteger
import com.massivecraft.massivecore.mixin.MixinMessage
import org.bukkit.Bukkit

/**
 * This is the command that gives crate keys to players
 * on the server. This is a very dangerous command. :O
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
class CmdGiveAll : CrateCommand() {

    init {
        aliases = listOf("giveall")
        addParameter(TypeCrate.get(), "crate")
        addParameter(TypeInteger.get(), "amount")
        addParameter(TypeBooleanTrue.get(), "noAlts", "false")
        addRequirements<CmdGiveAll>(RequirementHasPerm.get(Perm.GIVEALL))
    }

    override fun perform() {
        val crate = readArg<String>()
        val amount = readArg<Int>()
        val noAlts = readArg<Boolean>(false)
        val players = getPlayers(noAlts)

        Crates.i.crateByName(crate)?.let { found ->
            players.forEach {
                it!!.addKeys(found.crateId, amount)
            }

            msg(MConf.i.giveAll
                    .replace("{prefix}", MConf.i.prefix)
                    .replace("{player-size}", players.size.toString())
                    .replace("{amount}", amount.toString())
                    .replace("{crate-display-name}", found.displayName))

            players.forEach {
                if (!it!!.isOffline) {
                    MixinMessage.get().msgOne(it.player, MConf.i.giveAllNotify
                            .replace("{prefix}", MConf.i.prefix)
                            .replace("{amount}", amount.toString())
                            .replace("{crate-display-name}", found.displayName))
                }
            }
        } ?: msg(MConf.i.crateNotFound
                .replace("{prefix}", MConf.i.prefix)
                .replace("{crate-name}", crate))


    }

    /**
     * Gets a list of players on the server.
     * If [alts] is true, then it will return a list of all players except
     * for the first player with a matching IP address.
     * Then it will map them to a list of [MPlayer]s.
     *
     * @param alts Whether to return a list of alts.
     * @return A list of [MPlayer]s.
     *
     * TODO: Fix this function. :( LOL
     */

    private fun getPlayers(alts: Boolean = false): List<MPlayer?> {
        return Bukkit.getOnlinePlayers()
                .mapNotNull { MPlayer.get(it) }
                .groupBy { it.player.address }
                .values
                .flatMap { group ->
                    group.firstOrNull().let {
                        if (alts) group else listOf(it)
                    }
                }
    }


}