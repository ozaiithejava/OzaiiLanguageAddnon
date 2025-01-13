package org.ozaii.omclangaddnon.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.ozaii.omclangaddnon.utils.ConfigManager;

public class MessageUtils {

    private ConfigManager configManager;

    public MessageUtils(ConfigManager configManager) {
        this.configManager = configManager;
    }

    private static String globalPrefix;
    private String privatePrefix;
    private String privateColor;
    private static String globalColor;
    private static String playerColor;

    public void loadConfig() {
        globalPrefix = configManager.getString("prefixes.global");
        privatePrefix = configManager.getString("prefixes.private");
        privateColor = configManager.getString("colors.private");
        globalColor = configManager.getString("colors.global");
        playerColor = configManager.getString("colors.player");
    }

    public static void sendGlobalMessage(Player player, String message) {
        String playerName = player.getName();

        String formattedMessage = ColorUtils.applyColor(globalPrefix + " " + playerColor + playerName + " " + globalColor + message);

        Bukkit.broadcastMessage(formattedMessage);
    }

    public void sendPrivateMessage(Player sender, Player receiver, String message) {
        // Mesaj şablonlarını yapılandırma dosyasından alıyoruz
        String senderMessageTemplate = configManager.getString("messages.private-message-sender");
        String receiverMessageTemplate = configManager.getString("messages.private-message-receiver");

        // Mesaj formatlarını oyuncu adları ve mesaj ile dolduruyoruz
        String formattedSenderMessage = senderMessageTemplate
                .replace("{sender}", sender.getName())  // Gönderenin adı
                .replace("{message}", message)  // Mesaj
                .replace("{receiver}", receiver.getName());  // Alıcının adı

        String formattedReceiverMessage = receiverMessageTemplate
                .replace("{sender}", sender.getName())  // Gönderenin adı
                .replace("{message}", message)  // Mesaj
                .replace("{receiver}", receiver.getName());  // Alıcının adı

        // Renkleri uygula
        formattedSenderMessage = ColorUtils.applyColor(formattedSenderMessage);
        formattedReceiverMessage = ColorUtils.applyColor(formattedReceiverMessage);

        // Mesajları gönder
        sender.sendMessage(formattedSenderMessage);
        receiver.sendMessage(formattedReceiverMessage);
    }

    public String getFormattedMessage(String key) {
        String rawMessage = configManager.getString(key);
        if(rawMessage == null) {
            return "&c[HATA]: Mesaj bulunamadı!";
        }
        return ColorUtils.applyColor(rawMessage);
    }
    public int getFormattedMessageInt(String key) {

        return  configManager.getInt(key);
    }
}
