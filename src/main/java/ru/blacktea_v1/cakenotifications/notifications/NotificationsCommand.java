package ru.blacktea_v1.cakenotifications.notifications;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.blacktea_v1.cakenotifications.CakeNotifications;
import ru.blacktea_v1.cakenotifications.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author blacktea_v1
 * @date ⭐ 30.12.2023 | 19:14⭐
 */
public class NotificationsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        // if the arguments are zero, a message for help is sent to the player
        if (args.length == 0) {
            commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "listntf": {

                // getting notifications in mysql
                List<String> notifications = CakeNotifications.getInstance().getNotificationApi().getKeys();

                for (String ntf : notifications) {
                    // sending a message to the player
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.sampleNotificationList")
                            .replace("%index", String.valueOf(notifications.indexOf(ntf)))
                            .replace("%notification", ntf)));
                }

                break;
            }
            case "send": {

                // if the arguments are not three, a message for help is sent to the player
                if (args.length != 3) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                    break;
                }

                // getting player
                Player player = Bukkit.getPlayer(args[1]);

                // if player is none , command is break
                if (player == null) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.nonePlayer")));
                    break;
                }

                // if notification is none , command is break
                if (!CakeNotifications.getInstance().getNotificationApi().isExistsNotification(args[2])) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.noneNotification")));
                    break;
                }

                // getting commands & price
                List<String> cmd = CakeNotifications.getInstance().getNotificationApi().getCommands(args[2]);
                int price = CakeNotifications.getInstance().getNotificationApi().getPrice(args[2]);

                // execute commands
                executeCmd(cmd, player);
                CakeNotifications.getInstance().getPriceManager().addPrice(player.getUniqueId(), price);

                break;
            }
            case "create": {

                // if the arguments are not four, a message for help is sent to the player
                if (args.length != 3) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                    break;
                }

                // if notification is not none , command is break
                if (CakeNotifications.getInstance().getNotificationApi().isExistsNotification(args[1])) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.notificationIsExists")));
                    break;
                }

                // if argument two is not a number, command is break
                if (!isNumeric(args[2])) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.argumentIsNotNumber")));
                    break;
                }

                // getting notification name & price
                String notificationName = args[1];
                int price = Integer.parseInt(args[2]);

                // add in mysql
                CakeNotifications.getInstance().getNotificationApi().add(notificationName, price);

                // sending a message to the player
                commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.successfullyCreateNotification")));

                break;
            }
            case "delete": {

                // if the arguments are not one, a message for help is sent to the player
                if (args.length != 2) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                    break;
                }

                // if notification is none , command is break
                if (!CakeNotifications.getInstance().getNotificationApi().isExistsNotification(args[1])) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.noneNotification")));
                    break;
                }

                // delete notification in mysql
                CakeNotifications.getInstance().getNotificationApi().remove(args[1]);

                // sending a message to the player
                commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.successfullyDeleteNotification")));

                break;
            }
            case "remove": {

                // if the arguments are not one, a message for help is sent to the player
                if (args.length != 2) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                    break;
                }

                // getting player
                Player player = Bukkit.getPlayer(args[1]);

                // if player is none , command is break
                if (player == null) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.nonePlayer")));
                    break;
                }

                // remove player from mysql
                if (CakeNotifications.getInstance().getPriceApi().doesUserExist(player.getUniqueId())) {

                    // delete in mysql
                    CakeNotifications.getInstance().getPriceApi().remove(player.getUniqueId());

                    // sending a message to the player
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.successfullyRemoveFromDataBase").replace("%player", player.getName())));

                    break;
                }

                // sending a message to the player
                commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.notFoundInDataBase").replace("%player", player.getName())));

                break;
            }
            case "editprice": {

                // if the arguments are not three, a message for help is sent to the player
                if (args.length != 3) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                    break;
                }

                // if notification is none , command is break
                if (!CakeNotifications.getInstance().getNotificationApi().isExistsNotification(args[1])) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.noneNotification")));
                    break;
                }

                // if argument two is not a number, command is break
                if (!isNumeric(args[2])) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.argumentIsNotNumber")));
                    break;
                }

                // price change
                int price = Integer.parseInt(args[2]);
                CakeNotifications.getInstance().getNotificationApi().setPrice(args[1], price);

                // sending a message to the player
                commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.successfullySetPrice")));

                break;
            }
            case "addcmd": {

                // if there are fewer than four arguments, a message for help is sent to the player
                if (args.length < 3) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                    break;
                }

                // if notification is none , command is break
                if (!CakeNotifications.getInstance().getNotificationApi().isExistsNotification(args[1])) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.noneNotification")));
                    break;
                }

                String cmd = "";

                switch (args[2].toLowerCase()) {
                    case "broadcast": {

                        // if the arguments are not five, a message for help is sent to the player
                        if (args.length != 5) {
                            commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                            break;
                        }

                        // assign a value
                        cmd = "broadcast:"+args[3] + ";" + args[4];

                        break;
                    }
                    case "command": {

                        // if the arguments are not five, a message for help is sent to the player
                        if (args.length < 4) {
                            commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                            break;
                        }

                        // getting arguments after 3 arguments
                        StringBuilder sb = new StringBuilder();
                        for (int i = 3; i < args.length; i++) sb.append(args[i]).append(' ');
                        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
                        String text = sb.toString();

                        // assign a value
                        cmd = "command:"+text;

                        break;
                    }
                    case "telegram": {

                        // if the arguments are not five, a message for help is sent to the player
                        if (args.length < 4) {
                            commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                            break;
                        }

                        // getting arguments after 3 arguments
                        StringBuilder sb = new StringBuilder();
                        for (int i = 3; i < args.length; i++) sb.append(args[i]).append(' ');
                        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
                        String text = sb.toString();

                        // assign a value
                        cmd = "telegram:"+text;

                        break;
                    }
                    case "message": {

                        // if the arguments are not five, a message for help is sent to the player
                        if (args.length < 4) {
                            commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                            break;
                        }

                        // getting arguments after 3 arguments
                        StringBuilder sb = new StringBuilder();
                        for (int i = 3; i < args.length; i++) sb.append(args[i]).append(' ');
                        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
                        String text = sb.toString();

                        // assign a value
                        cmd = "message:"+text;

                        break;
                    }
                }

                // if cmd is none , command is break
                if (cmd.isEmpty()) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.underCommandIsNotFound")));
                    break;
                }

                // adding a command
                CakeNotifications.getInstance().getNotificationApi().addCommand(args[1], cmd);

                // sending a message to the player about the successful addition of a team to the list
                commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.successfullyAddCommand")));

                break;
            }

            case "delcmd": {

                // if the arguments are not three, a message for help is sent to the player
                if (args.length != 3) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                    break;
                }

                if (!CakeNotifications.getInstance().getNotificationApi().isExistsNotification(args[1])) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.noneNotification")));
                    break;
                }

                // if argument two is not a number, command is break
                if (!isNumeric(args[2])) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.argumentIsNotNumber")));
                    break;
                }

                try {

                    // delete command in notification
                    int index = Integer.parseInt(args[2]);
                    CakeNotifications.getInstance().getNotificationApi().removeCommand(args[1], index);

                    // sending a message to the player
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.successfullyDeleteCommand")));

                } catch (java.lang.IndexOutOfBoundsException e) {

                    // if command is not have in notification, a message sent to the player
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.noneCommandInNotification")));

                    break;
                }

                break;
            }
            case "listcmd": {

                // if the arguments are not three, a message for help is sent to the player
                if (args.length != 2) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
                    break;
                }

                // if notification is none , command is break
                if (!CakeNotifications.getInstance().getNotificationApi().isExistsNotification(args[1])) {
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.noneNotification")));
                    break;
                }

                // getting all cmd in notification
                List<String> commands = CakeNotifications.getInstance().getNotificationApi().getCommands(args[1]);
                for (String cmd : commands) {
                    // sending a message to the player
                    commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.sampleCommandList")
                            .replace("%index", String.valueOf(commands.indexOf(cmd)))
                            .replace("%command", cmd)));
                }

                break;
            }
            case "reload": {
                // reload config
                CakeNotifications.getInstance().reloadConfig();

                // sending a message to the player
                commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.successfullyReload")));

                break;
            }

            // if the argument is not found, a help message is displayed
            default:
                commandSender.sendMessage(Utils.hex(CakeNotifications.getInstance().getConfig().getString("messages.help")));
        }

        return false;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    
    public void executeCmd(List<String> commands, Player player) {
        List<String> telegramMessages = new ArrayList<>();

        for (String cmd : commands) {
            // creating a list
            String[] i = Utils.hex(cmd).replace("%player", player.getName()).split(":");

            // running through the list
            switch (i[0].toLowerCase()) {
                case "telegram": {
                    telegramMessages.add(i[1]);
                    break;
                }
                case "message": {
                    Bukkit.broadcastMessage(i[1]);
                    break;
                }
                case "broadcast": {
                    String[] bc = i[1].split(";");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendTitle(bc[0], bc[1]);
                    }
                    break;
                }
                case "command": {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), i[1]);
                    break;
                }
            }
        }
        // TODO (telegram logic)
    }
}
