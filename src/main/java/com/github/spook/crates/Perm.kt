package com.github.spook.crates

import com.massivecraft.massivecore.Identified
import com.massivecraft.massivecore.util.PermissionUtil
import org.bukkit.permissions.Permissible

/**
 * Represents a permission that can be used by this plugin.
 * All permissions are created by this class.
 *
 * @author spook
 *
 * Updates:
 * - 10/02/2023: spook - Initial creation.
 * - 10/08/2023: spook - Added WATCH permission.
 */
enum class Perm : Identified {
    BASECOMMAND,
    GIVE,
    GIVEALL,
    PLACE,
    WATCH,
    GENERATE,
    VERSION;

    private val id = PermissionUtil.createPermissionId(CratesPlugin.instance, this)

    override fun getId(): String {
        return id
    }

    fun has(permissible: Permissible, verbose: Boolean): Boolean {
        return PermissionUtil.hasPermission(permissible, this, verbose)
    }

    fun has(permissible: Permissible): Boolean {
        return PermissionUtil.hasPermission(permissible, this)
    }
}