package org.ozaii.omclangaddnon.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.ozaii.omclangaddnon.factorys.GlobalMessageFactory;
import org.ozaii.omclangaddnon.factorys.PlayerPrivateFactory;
import org.ozaii.omclangaddnon.utils.CooldownGlobal;
import org.ozaii.omclangaddnon.utils.ConfigManager;
import org.ozaii.omclangaddnon.utils.MessageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalMessageCommand implements CommandExecutor, TabCompleter {

    private CooldownGlobal cooldownGlobal;
    private MessageUtils messageUtils;
    private GlobalMessageFactory privateFactory;

    public GlobalMessageCommand(ConfigManager configManager) {
        this.cooldownGlobal = CooldownGlobal.getInstance();  // Cooldown instantiation
        this.messageUtils = new MessageUtils(configManager);  // MessageUtils instantiation
        this.privateFactory = new GlobalMessageFactory();  // Private message factory
        messageUtils.loadConfig();  // Config loading
    }

    private String getRemainingCooldownTime(Player player) {
        long remaining = cooldownGlobal.getRemainingCooldown(player.getName());
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
        GlobalMessageFactory messageFactory = new GlobalMessageFactory();
        if (!messageFactory.isGlobalMessagesEnabled(String.valueOf(player.getUniqueId()))){
            player.sendMessage(messageUtils.getFormattedMessage("messages.open-global-message"));
            return true;
        }
        if (cooldownGlobal.isCooldownActive(player.getName())) {
            String cooldownMessage = messageUtils.getFormattedMessage("messages.cooldown")
                    .replace("{time}", getRemainingCooldownTime(player));
            player.sendMessage(cooldownMessage);
            System.out.println("Cooldown halen aktif, mesaj yollanamaz.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(messageUtils.getFormattedMessage("messages.global-usage"));
            return true;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        if (!player.hasPermission("globalmessage.use")) {
            player.sendMessage(messageUtils.getFormattedMessage("messages.no-permission"));
            return true;
        }


        for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
            if (!privateFactory.isGlobalMessagesEnabled(targetPlayer.getUniqueId().toString())) {
                continue;
            }

            messageUtils.sendGlobalMessage(player, message);
        }


        int ctime = 30;
        try {
            ctime = messageUtils.getFormattedMessageInt("cooldowns.global-time");
        } catch (NumberFormatException e) {
            player.sendMessage(messageUtils.getFormattedMessage("messages.error-invalid-cooldown-time"));
            return true;
        }

        if (messageUtils.getFormattedMessage("cooldowns.enabled").contains("true")) {
            cooldownGlobal.setCooldown(player.getName(), ctime);
            System.out.println("Cooldown ayarlandı: " + ctime + " saniye");
        }

        player.sendMessage(messageUtils.getFormattedMessage("messages.global-message-sent"));
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
