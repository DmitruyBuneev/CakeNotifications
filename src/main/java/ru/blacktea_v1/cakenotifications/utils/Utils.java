package ru.blacktea_v1.cakenotifications.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author blacktea_v1
 * @date ⭐ 30.12.2023 | 19:23⭐
 */
public class Utils {
    private static Pattern pattern = Pattern.compile("(#[a-fA-F0-9]{6})");

    public static String hex(String message) {
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message).replace('&', '§');
    }

    public static List<String> hexList(List<String> message) {
        List<String> list = new ArrayList<>();
        for (String str : message) {
            list.add(hex(str));
        }
        return list;
    }

}
