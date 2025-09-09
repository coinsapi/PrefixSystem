package de.coinsapi.prefixSystem.manager

import de.coinsapi.prefixSystem.PrefixSystem
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStreamReader

class ConfigManager(private val plugin: PrefixSystem) {

    private lateinit var config: FileConfiguration
    private lateinit var configFile: File

    var chatFormat: String = ""
    var tabFormat: String = ""

    fun loadConfig() {
        configFile = File(plugin.dataFolder, "config.yml")

        if (!configFile.exists()) {
            plugin.dataFolder.mkdirs()
            plugin.saveResource("config.yml", false)
        }

        config = YamlConfiguration.loadConfiguration(configFile)

        val defaultConfig = YamlConfiguration.loadConfiguration(
            InputStreamReader(plugin.getResource("config.yml")!!)
        )
        config.setDefaults(defaultConfig)
        config.options().copyDefaults(true)
        saveConfig()

        loadValues()
    }

    private fun loadValues() {
        chatFormat = config.getString("formats.chat", "%prefix% &8| &7%playername% &8-> &7%message%")!!
        tabFormat = config.getString("formats.tab", "%prefix% &8| &7%playername%")!!
    }

    fun saveConfig() {
        config.save(configFile)
    }

    fun reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile)
        loadValues()
    }
}