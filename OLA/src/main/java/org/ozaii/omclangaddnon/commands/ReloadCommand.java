package org.ozaii.omclangaddnon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public ReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("ola.reload")) {
            plugin.reloadConfig();
            sender.sendMessage("Plugin başarıyla yeniden yüklendi!");
        } else {
            sender.sendMessage("Bu komutu kullanma izniniz yok.");
        }
        return true;
    }
}
