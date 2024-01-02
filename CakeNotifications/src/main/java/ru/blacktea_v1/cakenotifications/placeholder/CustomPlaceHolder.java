package ru.blacktea_v1.cakenotifications.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.blacktea_v1.cakenotifications.CakeNotifications;

/**
 * @author blacktea_v1
 * @date ⭐ 31.12.2023 | 15:25⭐
 */
public class CustomPlaceHolder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "cakenotifications";
    }

    @Override
    public @NotNull String getAuthor() {
        return "blacktea_v1";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {

        // if params equals price, return meaning from database
        if (params.toLowerCase().equals("price")) {

            // get price from database
            String price = String.valueOf(CakeNotifications.getInstance().getPriceManager().getPriceMap().get(player.getUniqueId()));

            if (price == null || price.equals("null")) {

                // if meaning from database is null, return 0
                return "0";
            }

            // return price
            return price;
        }

        // if params is air, return null
        return "null";
    }
}
