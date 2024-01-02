package ru.blacktea_v1.cakenotifications.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.blacktea_v1.cakenotifications.CakeNotifications;

import java.util.UUID;

/**
 * @author blacktea_v1
 * @date ⭐ 31.12.2023 | 15:16⭐
 */
public class ListenerOnJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        if (CakeNotifications.getInstance().getPriceManager().getPriceMap().get(uuid) == null) {
            if (CakeNotifications.getInstance().getPriceApi().doesUserExist(uuid)) {
                CakeNotifications.getInstance().getPriceManager().getPriceMap().put(uuid, CakeNotifications.getInstance().getPriceApi().getPrice(uuid));
            }
        }
    }
}
