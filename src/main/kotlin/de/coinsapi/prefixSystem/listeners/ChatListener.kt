package de.coinsapi.prefixSystem.listeners

import de.coinsapi.prefixSystem.PrefixSystem
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class ChatListener(private val plugin: PrefixSystem) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onChat(event: AsyncChatEvent) {
        val player = event.player
        val message = PlainTextComponentSerializer.plainText().serialize(event.message())

        val prefix = plugin.luckPermsManager.getPrefix(player)

        var formattedMessage = plugin.configManager.chatFormat
            .replace("%prefix%", prefix)
            .replace("%playername%", player.name)
            .replace("%message%", message)

        event.renderer { _, _, _, _ ->
            plugin.parseMessage(formattedMessage)
        }
    }
}