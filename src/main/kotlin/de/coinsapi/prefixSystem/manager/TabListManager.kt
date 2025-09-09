package de.coinsapi.prefixSystem.manager

import de.coinsapi.prefixSystem.PrefixSystem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class TabListManager(private val plugin: PrefixSystem) {

    private var updateTask: BukkitTask? = null

    fun startTabListUpdater() {
        updateTask = object : BukkitRunnable() {
            override fun run() {
                updateAllTabLists()
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L)
    }

    fun stopTabListUpdater() {
        updateTask?.cancel()
    }

    private fun updateAllTabLists() {
        val onlinePlayers = Bukkit.getOnlinePlayers().toList()
        val sortedPlayers = sortPlayersByWeight(onlinePlayers)

        for (viewer in onlinePlayers) {
            updateTabListForPlayer(viewer, sortedPlayers)
        }
    }

    private fun sortPlayersByWeight(players: List<Player>): List<Player> {
        return players.sortedByDescending { player ->
            plugin.luckPermsManager.getWeight(player)
        }
    }

    private fun updateTabListForPlayer(viewer: Player, sortedPlayers: List<Player>) {
        Bukkit.getScheduler().runTask(plugin, Runnable {
            for (target in sortedPlayers) {
                val prefix = plugin.luckPermsManager.getPrefix(target)

                var displayName = plugin.configManager.tabFormat
                    .replace("%prefix%", prefix)
                    .replace("%playername%", target.name)

                val component = plugin.parseMessage(displayName)
                target.playerListName(component)
            }
        })
    }

    fun updateSinglePlayer(player: Player) {
        val prefix = plugin.luckPermsManager.getPrefix(player)

        var displayName = plugin.configManager.tabFormat
            .replace("%prefix%", prefix)
            .replace("%playername%", player.name)

        val component = plugin.parseMessage(displayName)
        player.playerListName(component)
    }
}