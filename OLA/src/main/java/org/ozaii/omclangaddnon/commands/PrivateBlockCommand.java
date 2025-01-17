package org.ozaii.omclangaddnon.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ozaii.omclangaddnon.factorys.PlayerPrivateFactory;

import java.util.concurrent.CompletableFuture;

public class PrivateBlockCommand implements CommandExecutor {

    private final PlayerPrivateFactory playerPrivateFactory;

    public PrivateBlockCommand() {
        this.playerPrivateFactory = new PlayerPrivateFactory();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Komutu yalnızca oyuncular çalıştırabilir
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Bu komutu sadece oyuncular kullanabilir.");
            return false;
        }

        Player player = (Player) sender;

        // İki argüman gerekmekte: /private-block <playerName>
        if (args.length != 1) {
            player.sendMessage(ChatColor.YELLOW + "Lütfen geçerli bir oyuncu adı girin: /private-block <playerName>");
            return false;
        }

        String targetPlayerName = args[0];
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);

        // Kendini engellemeyi kontrol et
        if (targetPlayer != null && targetPlayer.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Kendinizi engelleyemezsiniz.");
            return true; // Bu durumda usage mesajı gösterilmez
        }

        // Çevrimdışı oyuncu engellenemez
        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage(ChatColor.RED + "Belirtilen oyuncu çevrimdışı veya bulunamadı.");
            return true; // Bu durumda usage mesajı gösterilmez
        }

        // Engellenen oyuncunun UUID'si
        String targetUUID = targetPlayer.getUniqueId().toString();

        // Asynchronous check if the player has already blocked the target player
        playerPrivateFactory.isPlayerBlocked(player.getUniqueId().toString(), targetUUID).thenAccept(isBlocked -> {
            if (isBlocked) {
                player.sendMessage(ChatColor.RED + targetPlayerName + " zaten engellenmiş.");
            } else {
                // Engelleme işlemi
                blockPlayer(player.getUniqueId().toString(), targetUUID).thenAccept(success -> {
                    if (success) {
                        player.sendMessage(ChatColor.GREEN + targetPlayerName + " başarıyla engellendi.");
                    } else {
                        player.sendMessage(ChatColor.RED + "Bir hata oluştu, oyuncuyu engelleyemedik.");
                    }
                });
            }
        });

        return true; // Komutun başarıyla tamamlandığını belirtmek için true döndürüyoruz
    }

    /**
     * Oyuncuyu engelleme işlemi
     * @param playerUUID Engelleyen oyuncunun UUID'si
     * @param blockedUUID Engellenen oyuncunun UUID'si
     * @return CompletableFuture<Boolean> Engelleme işleminin başarılı olup olmadığı
     */
    private CompletableFuture<Boolean> blockPlayer(String playerUUID, String blockedUUID) {
        return playerPrivateFactory.blockPlayer(playerUUID, blockedUUID).thenApply(aVoid -> true);
    }
}
