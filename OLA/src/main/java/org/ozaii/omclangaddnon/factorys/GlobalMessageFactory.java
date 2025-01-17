package org.ozaii.omclangaddnon.factorys;

import org.ozaii.omclangaddnon.OLA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class GlobalMessageFactory {

    private final String tableName;

    public GlobalMessageFactory() {
        // Config üzerinden tablo adını al
        this.tableName = OLA.getInstance().getConfigManager().getString("database.globalMessageTable");
        createTableIfNotExists();
    }

    /**
     * Tablo yoksa oluştur.
     */
    private void createTableIfNotExists() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "player_uuid VARCHAR(36) PRIMARY KEY, " +
                "accept_global_messages BOOLEAN DEFAULT TRUE" +
                ");";

        OLA.getInstance().getDatabaseFactory().getConnectionAsync().thenAccept(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Tablo oluşturulurken bir hata oluştu: " + tableName);
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
     * Oyuncunun global mesaj alma durumunu kontrol eder.
     * @param playerUUID Oyuncunun UUID'si
     * @return CompletableFuture<Boolean> Global mesaj alıp almadığı
     */
    public CompletableFuture<Boolean> isGlobalMessagesEnabled(String playerUUID) {
        String query = "SELECT accept_global_messages FROM " + tableName + " WHERE player_uuid = ?;";

        return OLA.getInstance().getDatabaseFactory().getConnectionAsync().thenCompose(connection -> CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerUUID);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getBoolean("accept_global_messages");
                } else {
                    // Kayıt yoksa varsayılan değeri ekle ve döndür
                    setGlobalMessagesEnabled(playerUUID, true);
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
     * Oyuncunun global mesaj alma durumunu günceller.
     * @param playerUUID Oyuncunun UUID'si
     * @param isEnabled Yeni durum
     */
    public CompletableFuture<Void> setGlobalMessagesEnabled(String playerUUID, boolean isEnabled) {
        String insertOrUpdateQuery = "INSERT INTO " + tableName +
                " (player_uuid, accept_global_messages) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE accept_global_messages = VALUES(accept_global_messages);";

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
}
