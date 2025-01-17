package org.ozaii.omclangaddnon.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.ozaii.omclangaddnon.commands.PrivateMessageCommand;
import org.ozaii.omclangaddnon.factorys.GlobalMessageFactory;
import org.ozaii.omclangaddnon.factorys.PlayerPrivateFactory;
import org.ozaii.omclangaddnon.utils.ConfigManager;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ExpercoinsPlaceholderExpansion extends PlaceholderExpansion {

    Plugin plugin;
    GlobalMessageFactory globalMessageFactory;
    PlayerPrivateFactory playerPrivateFactory;
    ConfigManager configManager;

    public ExpercoinsPlaceholderExpansion(Plugin plugin, GlobalMessageFactory globalMessageFactory, PlayerPrivateFactory playerPrivateFactory, ConfigManager configManager) {
        this.plugin = plugin;
        this.globalMessageFactory = globalMessageFactory;
        this.playerPrivateFactory = playerPrivateFactory;
        this.configManager = configManager;
    }



    public String getIdentifier() {
        return "OLA";
    }

    public String getAuthor() {
        return "ozaii";
    }

    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    public boolean canRegister() {
        return true;
    }

    public boolean persist() {
        return true;
    }

    public String onRequest(OfflinePlayer player, String identifier) {
        if (player == null) {
            return "";
        }

        switch (identifier) {
            case "globalMessageStatus":
                // Global mesajların durumunu almak için asenkron işlemi hemen senkron hale getiriyoruz
                try {
                    return String.valueOf(globalMessageFactory.isGlobalMessagesEnabled(String.valueOf(player.getUniqueId())).get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return "Error";
                }
            case "privateMessageStatus":
                return String.valueOf(playerPrivateFactory.isPrivateMessagesEnabled(String.valueOf(player.getUniqueId())));

            case "chatStatus":
                if (configManager.getBoolean("settings.chatstatus.enabled")) {
                    return "Sohbet Aktif";
                } else {
                    return "Sohbet DeAktif";
                }

            case "blockedPlayers":
                // Engellenen oyuncuları listele
                CompletableFuture<List<String>> blockedPlayersFuture = playerPrivateFactory.getBlockedPlayers(String.valueOf(player.getUniqueId()));
                try {
                    List<String> blockedPlayers = blockedPlayersFuture.get(); // Asenkron işlemi senkron hale getiriyoruz
                    if (blockedPlayers.isEmpty()) {
                        return "Hiçbir oyuncu engellenmemiş.";
                    } else {
                        return String.join(", ", blockedPlayers); // Engellenen oyuncuların isimlerini döndür
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return "Error";
                }

            default:
                return null;
        }
    }


}