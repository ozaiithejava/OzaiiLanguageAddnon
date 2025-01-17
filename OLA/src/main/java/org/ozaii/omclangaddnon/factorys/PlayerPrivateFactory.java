package org.ozaii.omclangaddnon.factorys;

import org.ozaii.omclangaddnon.OLA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerPrivateFactory {

    private final String privateMessageTable;
    private final String blockTable;

    public PlayerPrivateFactory() {
        // Config üzerinden tablo adlarını al
        this.privateMessageTable = OLA.getInstance().getConfigManager().getString("database.privateMessageTable");
        this.blockTable = OLA.getInstance().getConfigManager().getString("database.blockTable");
        createTablesIfNotExist();
    }

    /**
     * Tablo yoksa oluştur.
     */
    private void createTablesIfNotExist() {
        // Özel mesaj tablosu oluşturma sorgusu
        String createPrivateMessageTable = "CREATE TABLE IF NOT EXISTS " + privateMessageTable + " (" +
                "player_uuid VARCHAR(36) PRIMARY KEY, " +
                "accept_private_messages BOOLEAN DEFAULT TRUE" +
                ");";

        // Engelleme tablosu oluşturma sorgusu
        String createBlockTable = "CREATE TABLE IF NOT EXISTS " + blockTable + " (" +
                "player_uuid VARCHAR(36), " +
                "blocked_player_uuid VARCHAR(36), " +
                "PRIMARY KEY(player_uuid, blocked_player_uuid)" +
                ");";

        // Veritabanı bağlantısı üzerinden tablo oluşturma
        OLA.getInstance().getDatabaseFactory().getConnectionAsync().thenAccept(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(createPrivateMessageTable)) {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Tablo oluşturulurken bir hata oluştu: " + privateMessageTable);
            }

            try (PreparedStatement statement = connection.prepareStatement(createBlockTable)) {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Tablo oluşturulurken bir hata oluştu: " + blockTable);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Oyuncunun özel mesaj alma durumunu kontrol eder.
     * @param playerUUID Oyuncunun UUID'si
     * @return CompletableFuture<Boolean> Özel mesaj alıp almadığı
     */
    public CompletableFuture<Boolean> isPrivateMessagesEnabled(String playerUUID) {
        String query = "SELECT accept_private_messages FROM " + privateMessageTable + " WHERE player_uuid = ?;";

        return OLA.getInstance().getDatabaseFactory().getConnectionAsync().thenCompose(connection -> CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerUUID);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getBoolean("accept_private_messages");
                } else {
                    // Kayıt yoksa varsayılan değeri ekle ve döndür
                    setPrivateMessagesEnabled(playerUUID, true);
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Oyuncu durumu sorgulanırken bir hata oluştu: " + playerUUID);
                return true; // Hata durumunda varsayılan olarak açık döndür
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * Oyuncunun özel mesaj alma durumunu günceller.
     * @param playerUUID Oyuncunun UUID'si
     * @param isEnabled Yeni durum
     * @return CompletableFuture<Void> İşlem sonucu
     */
    public CompletableFuture<Void> setPrivateMessagesEnabled(String playerUUID, boolean isEnabled) {
        String insertOrUpdateQuery = "INSERT INTO " + privateMessageTable +
                " (player_uuid, accept_private_messages) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE accept_private_messages = VALUES(accept_private_messages);";

        return OLA.getInstance().getDatabaseFactory().getConnectionAsync().thenAccept(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(insertOrUpdateQuery)) {
                statement.setString(1, playerUUID);
                statement.setBoolean(2, isEnabled);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Oyuncu durumu güncellenirken bir hata oluştu: " + playerUUID);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Engellenen oyuncuları getirir.
     * @param playerUUID Oyuncunun UUID'si
     * @return CompletableFuture<List<String>> Engellenen oyuncuların UUID'leri
     */
    public CompletableFuture<List<String>> getBlockedPlayers(String playerUUID) {
        String query = "SELECT blocked_player_uuid FROM " + blockTable + " WHERE player_uuid = ?;"; // Burada blockTable kullanıyoruz

        return OLA.getInstance().getDatabaseFactory().getConnectionAsync().thenCompose(connection -> CompletableFuture.supplyAsync(() -> {
            List<String> blockedPlayers = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerUUID);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    blockedPlayers.add(resultSet.getString("blocked_player_uuid"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Engellenen oyuncular sorgulanırken bir hata oluştu: " + playerUUID);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return blockedPlayers;
        }));
    }

    /**
     * Oyuncunun belirli bir oyuncuyu engelleyip engellemediğini kontrol eder.
     * @param playerUUID Oyuncunun UUID'si
     * @param blockedUUID Engellenen oyuncunun UUID'si
     * @return CompletableFuture<Boolean> Engellenip engellenmediği durumu
     */
    public CompletableFuture<Boolean> isPlayerBlocked(String playerUUID, String blockedUUID) {
        String query = "SELECT 1 FROM " + blockTable + " WHERE player_uuid = ? AND blocked_player_uuid = ?;";

        return OLA.getInstance().getDatabaseFactory().getConnectionAsync().thenCompose(connection -> CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerUUID);
                statement.setString(2, blockedUUID);
                ResultSet resultSet = statement.executeQuery();

                return resultSet.next(); // Eğer sonuç varsa, oyuncu engellenmiştir
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Engelleme durumu sorgulanırken bir hata oluştu: " + playerUUID + " -> " + blockedUUID);
                return false; // Hata durumunda engellemeyi varsayalım
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * Oyuncunun başka bir oyuncuyu engellemesini sağlar.
     * @param playerUUID Oyuncunun UUID'si
     * @param blockedUUID Engellenen oyuncunun UUID'si
     * @return CompletableFuture<Void> İşlem sonucu
     */
    public CompletableFuture<Void> blockPlayer(String playerUUID, String blockedUUID) {
        String query = "INSERT INTO " + blockTable + " (player_uuid, blocked_player_uuid) VALUES (?, ?);"; // Burada blockTable kullanıyoruz

        return OLA.getInstance().getDatabaseFactory().getConnectionAsync().thenAccept(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerUUID);
                statement.setString(2, blockedUUID);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Oyuncu engellenirken bir hata oluştu: " + playerUUID + " -> " + blockedUUID);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public CompletableFuture<Void> unblockPlayer(String playerUUID, String blockedUUID) {
        String query = "DELETE FROM " + blockTable + " WHERE player_uuid = ? AND blocked_player_uuid = ?;";

        return OLA.getInstance().getDatabaseFactory().getConnectionAsync().thenAccept(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerUUID);
                statement.setString(2, blockedUUID);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Oyuncu engeli kaldırılırken bir hata oluştu: " + playerUUID + " -> " + blockedUUID);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
