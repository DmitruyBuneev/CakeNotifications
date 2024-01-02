package ru.blacktea_v1.cakenotifications.notifications;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.blacktea_v1.cakenotifications.CakeNotifications;

import java.util.Arrays;
import java.util.List;

/**
 * @author blacktea_v1
 * @date ⭐ 31.12.2023 | 10:23⭐
 */
public class NotificationsTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        // if the arguments are zero, return null
        if (args.length == 0) {
            return null;
        }

        // if the arguments are one, return the subcommand
        if (args.length == 1) {
            return Arrays.asList("send", "create", "delete", "editprice", "addcmd", "delcmd", "listcmd", "remove", "listntf");
        }

        // if the arguments are two, return the arguments for commands
        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "delcmd":
                case "addcmd":
                case "editprice":
                case "delete":
                case "listcmd":
                case "create":
                    return Arrays.asList("<notification name>");
            }
        }

        // if the arguments are three, return the arguments for commands
        if (args.length == 3) {
            switch (args[0].toLowerCase()) {
                case "send":
                    return Arrays.asList("<notification name>");
                case "editprice":
                case "create":
                    return Arrays.asList("<enter price>");
                case "addcmd":
                    return Arrays.asList("command", "broadcast", "message", "telegram");
                case "delcmd":
                    return Arrays.asList("<enter index>");
            }
        }

        // if the arguments are four, return the arguments for commands
        if (args.length == 4) {
            switch (args[2].toLowerCase()) {
                case "command":
                    return Arrays.asList("<enter command>");
                case "broadcast":
                    return Arrays.asList("<enter main titul>");
                case "message":
                    return Arrays.asList("<enter message>");
                case "telegram":
                    return Arrays.asList("<enter telegram message>");
            }
        }

        // if the arguments are five, return the arguments for commands
        if (args.length == 5) {
            if (args[2].toLowerCase().equals("broadcast")) {
                return Arrays.asList("<enter sub titul>");
            }
        }

        return null;
    }
}
