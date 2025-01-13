package org.ozaii.omclangaddnon;

import org.bukkit.plugin.java.JavaPlugin;
import org.ozaii.omclangaddnon.commands.ChatToggleCommand;
import org.ozaii.omclangaddnon.commands.GlobalMessageCommand;
import org.ozaii.omclangaddnon.commands.PrivateMessageCommand;
import org.ozaii.omclangaddnon.listener.ChatDisableListener;
import org.ozaii.omclangaddnon.utils.ConfigManager;
import org.ozaii.omclangaddnon.utils.MessageUtils;
import ozaii.apis.base.FactoryApi;
import ozaii.factory.DatabaseFactory;

public final class OLA extends JavaPlugin {
    private static OLA INSTANCE;
    private static ConfigManager config;
    private static MessageUtils msg;
    private static FactoryApi api = new FactoryApi();
    private static DatabaseFactory databasemanager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        // ConfigManager'ı başlatın
        try {
            config = new ConfigManager(this);
            config.setupConfig();  // Yapılandırma dosyasını yükleme
        } catch (Exception e) {
            getLogger().severe("Yapılandırma dosyası yüklenirken bir hata oluştu!");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;  // Plugin'i başlatmayı sonlandır
        }

        // MessageUtils'i başlatın
        try {
            msg = new MessageUtils(config);
            msg.loadConfig();  // Mesajları yükleme
        } catch (Exception e) {
            getLogger().severe("Mesaj dosyası yüklenirken bir hata oluştu!");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // DatabaseManager'ı başlatın
        try {
            databasemanager = api.getDatabaseFactory();
            if (databasemanager == null) {
                throw new NullPointerException("DatabaseFactory nesnesi null döndü!");
            }
        } catch (Exception e) {
            getLogger().severe("Veritabanı bağlantısı kurulurken bir hata oluştu!");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Komutların ve TabComplete işlemlerinin başlatılması
        PrivateMessageCommand privateMessageCommand = new PrivateMessageCommand(config);
        getCommand("pm").setExecutor(privateMessageCommand);
        getCommand("pm").setTabCompleter(privateMessageCommand);

        ChatToggleCommand privateMessageToggleCommand = new ChatToggleCommand(config);
        getCommand("allchat").setExecutor(privateMessageToggleCommand);
        getCommand("allchat").setTabCompleter(privateMessageToggleCommand);

        GlobalMessageCommand globalMessageCommand = new GlobalMessageCommand(config);
        getCommand("global").setExecutor(globalMessageCommand);
        getCommand("global").setTabCompleter(globalMessageCommand);

        getServer().getPluginManager().registerEvents(new ChatDisableListener(config,new MessageUtils(config)), this);

        getLogger().info("Plugin başarıyla etkinleştirildi.");
    }

    @Override
    public void onDisable() {
        // Plugin kapatıldığında yapılacak işlemler
        getLogger().info("Plugin kapatıldı.");
    }

    // Singleton instance
    public static OLA getInstance() {
        return INSTANCE;
    }

    // ConfigManager'a erişim
    public static ConfigManager getConfigManager() {
        return config;
    }

    // MessageUtils'e erişim
    public static MessageUtils getMessageUtils() {
        return msg;
    }

    // DatabaseFactory'e erişim
    public static DatabaseFactory getDatabaseFactory() {
        return databasemanager;
    }
}
