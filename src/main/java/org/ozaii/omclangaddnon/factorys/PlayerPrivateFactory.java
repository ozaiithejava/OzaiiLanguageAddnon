package org.ozaii.omclangaddnon.factorys;


import org.ozaii.omclangaddnon.OLA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerPrivateFactory {

    private final String tableName;

    public PlayerPrivateFactory() {
        // Config üzerinden tablo adını al
        this.tableName = OLA.getInstance().getConfigManager().getString("database.privateMessageTable");
        createTableIfNotExists();
    }

    /**
     * Tablo yoksa oluştur.
     */
    private void createTableIfNotExists() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "player_uuid VARCHAR(36) PRIMARY KEY, " +
                "accept_private_messages BOOLEAN DEFAULT TRUE" +
                ");";

        try (Connection connection = OLA.getInstance().getDatabaseFactory().getConnection();
             PreparedStatement statement = connection.prepareStatement(createTableQuery)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Tablo oluşturulurken bir hata oluştu: " + tableName);
        }
    }

    /**
     * Oyuncunun özel mesaj alma durumunu kontrol eder.
     * @param playerUUID Oyuncunun UUID'si
     * @return Özel mesaj alıp almadığı
     */
    public boolean isPrivateMessagesEnabled(String playerUUID) {
        String query = "SELECT accept_private_messages FROM " + tableName + " WHERE player_uuid = ?;";

        try (Connection connection = OLA.getInstance().getDatabaseFactory().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

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
        }
    }

    /**
     * Oyuncunun özel mesaj alma durumunu günceller.
     * @param playerUUID Oyuncunun UUID'si
     * @param isEnabled Yeni durum
     */
    public void setPrivateMessagesEnabled(String playerUUID, boolean isEnabled) {
        String insertOrUpdateQuery = "INSERT INTO " + tableName +
                " (player_uuid, accept_private_messages) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE accept_private_messages = VALUES(accept_private_messages);";

        try (Connection connection = OLA.getInstance().getDatabaseFactory().getConnection();
             PreparedStatement statement = connection.prepareStatement(insertOrUpdateQuery)) {

            statement.setString(1, playerUUID);
            statement.setBoolean(2, isEnabled);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Oyuncu durumu güncellenirken bir hata oluştu: " + playerUUID);
        }
    }
}
