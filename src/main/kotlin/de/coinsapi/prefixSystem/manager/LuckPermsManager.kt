package de.coinsapi.prefixSystem.manager

import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.user.User
import org.bukkit.entity.Player
import java.util.UUID

class LuckPermsManager {

    private val luckPerms: LuckPerms = LuckPermsProvider.get()

    fun getPrefix(player: Player): String {
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return ""
        return user.cachedData.metaData.prefix ?: ""
    }

    fun getWeight(player: Player): Int {
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return 0
        val primaryGroup = user.primaryGroup
        val group = luckPerms.groupManager.getGroup(primaryGroup) ?: return 0
        return group.weight.orElse(0)
    }

    fun getUserByUUID(uuid: UUID): User? {
        return luckPerms.userManager.getUser(uuid)
    }

    fun getPrefixByUUID(uuid: UUID): String {
        val user = getUserByUUID(uuid) ?: return ""
        return user.cachedData.metaData.prefix ?: ""
    }

    fun getWeightByUUID(uuid: UUID): Int {
        val user = getUserByUUID(uuid) ?: return 0
        val primaryGroup = user.primaryGroup
        val group = luckPerms.groupManager.getGroup(primaryGroup) ?: return 0
        return group.weight.orElse(0)
    }
}