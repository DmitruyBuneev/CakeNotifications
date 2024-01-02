package ru.blacktea_v1.cakenotifications.price.api.notification;

import java.util.List;

/**
 * @author blacktea_v1
 * @date ⭐ 01.01.2024 | 21:30⭐
 */
public interface INotificationApi {

    void add(String notificationName, List<String> commands, int price);

    void add(String notificationName, int price);

    void remove(String notificationName);

    boolean isExistsNotification(String notificationName);

    void addCommand(String notificationName, String command);

    void setPrice(String notificationName, int price);

    boolean removeCommand(String notificationName, int index);

    List<String> getCommands(String notificationName);

    int getPrice(String notificationName);

    List<String> getKeys();

}
