package org.ozaii.omclangaddnon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.ozaii.omclangaddnon.factorys.GlobalMessageFactory;
import org.ozaii.omclangaddnon.factorys.PlayerPrivateFactory;
import org.ozaii.omclangaddnon.utils.ConfigManager;
import org.ozaii.omclangaddnon.utils.MessageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatToggleCommand implements CommandExecutor, TabCompleter {

    private final PlayerPrivateFactory privateFactory;
    private final GlobalMessageFactory globalFactory;
    private final MessageUtils messageUtils;

    public ChatToggleCommand(ConfigManager configManager) {
        this.privateFactory = new PlayerPrivateFactory();
        this.globalFactory = new GlobalMessageFactory();
        this.messageUtils = new MessageUtils(configManager);
        messageUtils.loadConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messageUtils.getFormattedMessage("messages.only-players"));
            return false;
        }

        Player player = (Player) sender;
        String playerUUID = player.getUniqueId().toString();

        if (args.length < 2) {
            player.sendMessage(messageUtils.getFormattedMessage("commands.chat-toggle.usage"));
            return true;
        }

        String chatType = args[0].toLowerCase();
        String toggleOption = args[1].toLowerCase();

        switch (chatType) {
            case "private":
                handleToggle(player, toggleOption, playerUUID, privateFactory, "private");
                break;

            case "global":
                handleToggle(player, toggleOption, playerUUID, globalFactory, "global");
                break;

            default:
                player.sendMessage(messageUtils.getFormattedMessage("commands.chat-toggle.invalid-type"));
                break;
        }

        return true;
    }

    private void handleToggle(Player player, String toggleOption, String playerUUID, Object factory, String type) {
        boolean enable = toggleOption.equals("on");
        if (!(factory instanceof PlayerPrivateFactory || factory instanceof GlobalMessageFactory)) {
            return;
        }

        if (factory instanceof PlayerPrivateFactory) {
            ((PlayerPrivateFactory) factory).setPrivateMessagesEnabled(playerUUID, enable);
        } else if (factory instanceof GlobalMessageFactory) {
            ((GlobalMessageFactory) factory).setGlobalMessagesEnabled(playerUUID, enable);
        }

        String messageKey = enable ? "commands.chat-toggle." + type + ".enabled" : "commands.chat-toggle." + type + ".disabled";
        player.sendMessage(messageUtils.getFormattedMessage(messageKey));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            List<String> options = Arrays.asList("private", "global");
            for (String option : options) {
                if (option.startsWith(args[0].toLowerCase())) {
                    suggestions.add(option);
                }
            }
        } else if (args.length == 2) {
            List<String> options = Arrays.asList("on", "off");
            for (String option : options) {
                if (option.startsWith(args[1].toLowerCase())) {
                    suggestions.add(option);
                }
            }
        }
        return suggestions;
    }
}
