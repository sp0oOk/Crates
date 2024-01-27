package com.github.spook.crates.cmd.type

import com.github.spook.crates.entity.MPlayer
import com.github.spook.crates.entity.MPlayerColl
import com.massivecraft.massivecore.command.type.Type;

/**
 * This is a type that allows for tab completion of MPlayer names.
 *
 * @see com.github.spook.crates.entity.MPlayer
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 */
object TypeMPlayer {
    fun get(): Type<MPlayer> {
        return MPlayerColl.get().typeEntity
    }
}