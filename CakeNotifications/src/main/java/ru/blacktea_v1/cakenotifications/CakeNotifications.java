package ru.blacktea_v1.cakenotifications;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.blacktea_v1.cakenotifications.databases.MySql;
import ru.blacktea_v1.cakenotifications.handler.ListenerOnJoin;
import ru.blacktea_v1.cakenotifications.notifications.NotificationsCommand;
import ru.blacktea_v1.cakenotifications.notifications.NotificationsTabCompleter;
import ru.blacktea_v1.cakenotifications.placeholder.CustomPlaceHolder;
import ru.blacktea_v1.cakenotifications.price.PriceManager;
import ru.blacktea_v1.cakenotifications.price.api.notification.NotificationApi;
import ru.blacktea_v1.cakenotifications.price.api.price.PriceApi;

public final class CakeNotifications extends JavaPlugin {

    private static CakeNotifications instance;

    public static CakeNotifications getInstance() {
        return instance;
    }

    private MySql mySQL;

    public MySql getMySQL() {
        return mySQL;
    }

    private PriceManager priceManager;

    public PriceManager getPriceManager() {
        return priceManager;
    }

    private PriceApi priceApi;

    public PriceApi getPriceApi() {
        return priceApi;
    }

    private NotificationApi notificationApi;

    public NotificationApi getNotificationApi() {
        return notificationApi;
    }

    @Override
    public void onEnable() {

        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
            System.out.println(ChatColor.RED + "CakeNotifications: Не найден плагин PlaceHolderAPI");
            this.setEnabled(false);
            return;
        }

        instance = this;
        saveDefaultConfig();

        getCommand("notifications").setExecutor(new NotificationsCommand());
        getCommand("notifications").setTabCompleter(new NotificationsTabCompleter());

        this.mySQL = MySql.newBuilder()
                .withUrl(getConfig().getString("mysql.host"))
                .withPort(getConfig().getInt("mysql.port"))
                .withDatabase(getConfig().getString("mysql.database"))
                .withUser(getConfig().getString("mysql.user"))
                .withPassword(getConfig().getString("mysql.password"))
                .create();

        priceApi = new PriceApi();
        priceApi.createTables();

        notificationApi = new NotificationApi();
        notificationApi.createTables();

        this.priceManager = new PriceManager();
        Bukkit.getPluginManager().registerEvents(new ListenerOnJoin(), this);
        new CustomPlaceHolder().register();
    }
}
