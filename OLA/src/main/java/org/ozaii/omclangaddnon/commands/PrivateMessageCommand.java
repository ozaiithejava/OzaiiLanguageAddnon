package org.ozaii.omclangaddnon.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.ozaii.omclangaddnon.factorys.PlayerPrivateFactory;
import org.ozaii.omclangaddnon.utils.CooldownGlobal;
import org.ozaii.omclangaddnon.utils.ConfigManager;
import org.ozaii.omclangaddnon.utils.MessageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PrivateMessageCommand implements CommandExecutor, TabCompleter {

    private CooldownGlobal cooldown;
    private MessageUtils messageUtils;
    private PlayerPrivateFactory privateFactory;

    public PrivateMessageCommand(ConfigManager configManager) {
        this.cooldown = CooldownGlobal.getInstance();
        this.messageUtils = new MessageUtils(configManager);
        this.privateFactory = new PlayerPrivateFactory();
        messageUtils.loadConfig(); // Load settings
    }

    private String getRemainingCooldownTime(Player player) {
        long remaining = cooldown.getRemainingCooldown(player.getName());
        int minutes = (int) (remaining / 60);
        int seconds = (int) (remaining % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messageUtils.getFormattedMessage("messages.only-players"));
            return false;
        }

        Player player = (Player) sender;

        if (cooldown.isCooldownActive(player.getName())) {
            String cooldownMessage = messageUtils.getFormattedMessage("messages.pcooldown")
                    .replace("{time}", getRemainingCooldownTime(player));
            player.sendMessage(cooldownMessage);
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(messageUtils.getFormattedMessage("messages.usage"));
            return true;
        }

        String targetPlayerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        // Hedef oyuncu kontrolü
        if (targetPlayer == null) {
            player.sendMessage(messageUtils.getFormattedMessage("messages.player-not-found"));
            return true;
        }

        if (targetPlayer == sender) {
            player.sendMessage(messageUtils.getFormattedMessage("messages.self-message"));
            return true;
        }

        String targetUUID = targetPlayer.getUniqueId().toString();

        // Asynchronous check if private messages are enabled for the target player
        privateFactory.isPrivateMessagesEnabled(targetUUID).thenAccept(isEnabled -> {
            if (!isEnabled) {
                String noMessage = messageUtils.getFormattedMessage("messages.private-disabled")
                        .replace("{player}", targetPlayer.getName());
                player.sendMessage(noMessage);
                return;
            }

            // Check if the target player has blocked the sender
            privateFactory.isPlayerBlocked(targetUUID, player.getUniqueId().toString()).thenAccept(isBlocked -> {
                if (isBlocked) {
                    String blockedMessage = messageUtils.getFormattedMessage("messages.player-blocked")
                            .replace("{player}", targetPlayer.getName());
                    player.sendMessage(blockedMessage);
                    return;
                }

                // Eğer engellenmemişse, mesaj gönder
                String message = String.join(" ", Arrays.asList(args).subList(1, args.length)); // args.length() değil, args.length
                messageUtils.sendPrivateMessage(player, targetPlayer, message);

                // Cooldown ayarla
                int ctime = Integer.parseInt(messageUtils.getFormattedMessage("cooldowns.time"));
                if (messageUtils.getFormattedMessage("cooldowns.enabled").contains("true")) {
                    cooldown.setCooldown(player.getName(), ctime);
                }
            });
        });

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                suggestions.add(onlinePlayer.getName());
            }
        }
        return suggestions;
    }
}
