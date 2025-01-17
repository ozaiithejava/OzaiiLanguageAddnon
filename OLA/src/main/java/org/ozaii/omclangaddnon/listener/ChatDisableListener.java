package org.ozaii.omclangaddnon.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.ozaii.omclangaddnon.utils.ConfigManager;
import org.ozaii.omclangaddnon.utils.MessageUtils;

public class ChatDisableListener implements Listener {

    private final ConfigManager configManager;
    private final MessageUtils messageUtils;

    public ChatDisableListener(ConfigManager configManager, MessageUtils messageUtils) {
        this.configManager = configManager;
        this.messageUtils = messageUtils;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        boolean chatEnabled = configManager.getBoolean("settings.chatstatus.enabled");
        if (!chatEnabled) {
            String chatBypassPermission = configManager.getString("permissions.chat-bypass");

            if (!player.hasPermission(chatBypassPermission)) {
                event.setCancelled(true);

                String message = messageUtils.getFormattedMessage("messages.chat-disabled");
                player.sendMessage(message);
            }
        }
    }
}
