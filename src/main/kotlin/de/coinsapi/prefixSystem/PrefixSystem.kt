package de.coinsapi.prefixSystem

import de.coinsapi.prefixSystem.listeners.ChatListener
import de.coinsapi.prefixSystem.manager.ConfigManager
import de.coinsapi.prefixSystem.manager.LuckPermsManager
import de.coinsapi.prefixSystem.manager.TabListManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.plugin.java.JavaPlugin

class PrefixSystem : JavaPlugin() {

    lateinit var configManager: ConfigManager
    lateinit var luckPermsManager: LuckPermsManager
    lateinit var chatListener: ChatListener
    lateinit var tabListManager: TabListManager

    override fun onEnable() {
        configManager = ConfigManager(this)
        configManager.loadConfig()

        if (!setupLuckPerms()) {
            logger.severe("LuckPerms not found! Disabling plugin...")
            server.pluginManager.disablePlugin(this)
            return
        }

        luckPermsManager = LuckPermsManager()
        chatListener = ChatListener(this)
        tabListManager = TabListManager(this)

        server.pluginManager.registerEvents(chatListener, this)

        tabListManager.startTabListUpdater()

        logger.info("PrefixSystem has been enabled!")
    }

    override fun onDisable() {
        if (::tabListManager.isInitialized) {
            tabListManager.stopTabListUpdater()
        }
        logger.info("PrefixSystem has been disabled!")
    }

    private fun setupLuckPerms(): Boolean {
        return server.pluginManager.getPlugin("LuckPerms") != null
    }

    fun parseMessage(message: String): Component {
        val miniMessage = MiniMessage.miniMessage()
        val legacy = LegacyComponentSerializer.builder()
            .hexColors()
            .character('&')
            .extractUrls()
            .build()

        return try {
            miniMessage.deserialize(message)
        } catch (e: Exception) {
            try {
                legacy.deserialize(message)
            } catch (e2: Exception) {
                Component.text(message)
            }
        }
    }
}
