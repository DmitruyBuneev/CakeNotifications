package ru.blacktea_v1.cakenotifications.price.api.notification;

import ru.blacktea_v1.cakenotifications.CakeNotifications;
import ru.blacktea_v1.cakenotifications.databases.MySql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author blacktea_v1
 * @date ⭐ 01.01.2024 | 21:30⭐
 */
public class NotificationApi implements INotificationApi {

    private MySql mySQL;

    @Override
    public void add(String notificationName, List<String> commands, int price) {
        mySQL.update("INSERT INTO notifications (notification, command, price) VALUES (?,?,?)", notificationName, commands.toString(), price);
    }

    @Override
    public void add(String notificationName, int price) {
        mySQL.update("INSERT INTO notifications (notification, command, price) VALUES (?,?,?)", notificationName, "", price);
    }

    @Override
    public void remove(String notificationName) {
        mySQL.update("DELETE FROM notifications WHERE notification =?", notificationName);
    }

    @Override
    public boolean isExistsNotification(String notificationName) {
        String qry = "SELECT count(*) AS count FROM notifications WHERE notification=?";
        try (ResultSet rs = mySQL.query(qry, notificationName)) {
            if (rs.next()) {
                return rs.getInt("count") != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void addCommand(String notificationName, String command) {
        try (ResultSet rs = mySQL.query("SELECT command FROM notifications WHERE notification = ?", notificationName)) {
            while (rs.next()) {
                String cmd = rs.getString("command");
                List<String> commands = getListFromString(cmd);
                commands.add(command);

                mySQL.update("UPDATE notifications SET command = ? WHERE notification = ?", commands.toString(), notificationName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPrice(String notificationName, int price) {
        mySQL.update("UPDATE notifications SET price = ? WHERE notification = ?", price, notificationName);
    }

    @Override
    public boolean removeCommand(String notificationName, int index) {
            try (ResultSet rs = mySQL.query("SELECT command FROM notifications WHERE notification = ?", notificationName);) {
                if (rs.next()) {
                    String cmd = rs.getString("command");
                    List<String> commands = getListFromString(cmd);
                    commands.remove(index);

                    mySQL.update("UPDATE notifications SET command = ? WHERE notification = ?", commands.toString(), notificationName);
                    return true;
                }
            } catch (SQLException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        return false;
    }

    @Override
    public List<String> getCommands(String notificationName) {
            try (ResultSet rs = mySQL.query("SELECT command FROM notifications WHERE notification = ?", notificationName);) {
                if (rs.next()) {
                    String cmd = rs.getString("command");
                    return getListFromString(cmd);
                }
            } catch (SQLException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        return null;
    }

    @Override
    public int getPrice(String notificationName) {
            try (ResultSet rs = mySQL.query("SELECT price FROM notifications WHERE notification = ?", notificationName)) {
                if (rs.next()) {
                    return rs.getInt("price");
                }
            } catch (SQLException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        return 0;
    }

    @Override
    public List<String> getKeys() {

        String qry = "SELECT notification FROM notifications";
        List<String> keys = new ArrayList<>();

        try (ResultSet rs = mySQL.query(qry)) {
            while (rs.next()) {
                String notification = rs.getString("notification");
                keys.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return keys;
    }

    public void createTables() {
        this.mySQL = CakeNotifications.getInstance().getMySQL();
        mySQL.update("CREATE TABLE IF NOT EXISTS notifications (notification VARCHAR(20), command VARCHAR(500), price INT(35))");
    }

    public List<String> getListFromString(String commands) {

        if (commands.isEmpty()) {
            return new ArrayList<>();
        }

        commands = commands.substring(1, commands.length() - 1);
        String[] items = commands.split(",\\s");
        List<String> list = Arrays.asList(items);

        return new ArrayList<>(list);
    }
}
