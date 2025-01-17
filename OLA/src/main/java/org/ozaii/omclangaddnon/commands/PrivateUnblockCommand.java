package org.ozaii.omclangaddnon.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ozaii.omclangaddnon.factorys.PlayerPrivateFactory;

import java.util.concurrent.CompletableFuture;

public class PrivateUnblockCommand implements CommandExecutor {

    private final PlayerPrivateFactory playerPrivateFactory;

    public PrivateUnblockCommand() {
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

        // İki argüman gerekmekte: /private-unblock <playerName>
        if (args.length != 1) {
            player.sendMessage(ChatColor.YELLOW + "Lütfen geçerli bir oyuncu adı girin: /private-unblock <playerName>");
            return false;
        }

        String targetPlayerName = args[0];
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage(ChatColor.RED + "Belirtilen oyuncu çevrimdışı veya bulunamadı.");
            return false;
        }

        // Engellenen oyuncunun UUID'si
        String targetUUID = targetPlayer.getUniqueId().toString();

        // Engellenmiş olup olmadığını kontrol et
        isPlayerBlocked(player.getUniqueId().toString(), targetUUID).thenAccept(isBlocked -> {
            if (!isBlocked) {
                player.sendMessage(ChatColor.RED + "Bu kişi zaten engellenmiş değil.");
                return;
            }

            // Engelleme kaldırma işlemi
            unblockPlayer(player.getUniqueId().toString(), targetUUID).thenAccept(success -> {
                if (success) {
                    player.sendMessage(ChatColor.GREEN + targetPlayerName + " başarıyla engelinizden çıkarıldı.");
                } else {
                    player.sendMessage(ChatColor.RED + "Bir hata oluştu, oyuncunun engelini kaldıramadık.");
                }
            });
        });

        return true; // Komutun başarıyla tamamlandığını belirtmek için true döndürüyoruz
    }

    /**
     * Oyuncunun engellenip engellenmediğini kontrol etme
     * @param playerUUID Engelleyen oyuncunun UUID'si
     * @param blockedUUID Engellenen oyuncunun UUID'si
     * @return CompletableFuture<Boolean> Engel durumu (true = engellenmiş, false = engellenmemiş)
     */
    private CompletableFuture<Boolean> isPlayerBlocked(String playerUUID, String blockedUUID) {
        return playerPrivateFactory.isPlayerBlocked(playerUUID, blockedUUID);
    }

    /**
     * Oyuncunun engelini kaldırma işlemi
     * @param playerUUID Engelleyen oyuncunun UUID'si
     * @param blockedUUID Engellenen oyuncunun UUID'si
     * @return CompletableFuture<Boolean> Engel kaldırma işleminin başarılı olup olmadığı
     */
    private CompletableFuture<Boolean> unblockPlayer(String playerUUID, String blockedUUID) {
        return playerPrivateFactory.unblockPlayer(playerUUID, blockedUUID).thenApply(aVoid -> true);
    }
}
