package ru.blacktea_v1.cakenotifications.price;

import ru.blacktea_v1.cakenotifications.CakeNotifications;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author blacktea_v1
 * @date ⭐ 31.12.2023 | 15:05⭐
 */
public class PriceManager {

    private HashMap<UUID, Integer> priceMap = new HashMap<>();

    public HashMap<UUID, Integer> getPriceMap() {
        return priceMap;
    }

    public void addPrice(UUID uuid, int price) {
        if (!CakeNotifications.getInstance().getPriceApi().doesUserExist(uuid)) {
            CakeNotifications.getInstance().getPriceApi().initPlayer(uuid);
        }

        if (priceMap.get(uuid) == null) {
            CakeNotifications.getInstance().getPriceApi().addPrice(uuid, price);
            priceMap.put(uuid, CakeNotifications.getInstance().getPriceApi().getPrice(uuid));
            return;
        }

        CakeNotifications.getInstance().getPriceApi().addPrice(uuid, price);
        priceMap.put(uuid, CakeNotifications.getInstance().getPriceApi().getPrice(uuid));
    }

    public void remove(UUID uuid) {
        priceMap.remove(uuid);
        CakeNotifications.getInstance().getPriceApi().remove(uuid);
    }
}
