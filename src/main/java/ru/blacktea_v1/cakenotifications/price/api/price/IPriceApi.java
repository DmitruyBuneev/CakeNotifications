package ru.blacktea_v1.cakenotifications.price.api.price;

import java.util.UUID;

/**
 * @author blacktea_v1
 * @date ⭐ 31.12.2023 | 14:59⭐
 */
public interface IPriceApi {

    Integer getPrice(UUID uuid);

    void addPrice(UUID uuid, int amount);

    void removePrice(UUID uuid, int amount);

    void setPrice(UUID uuid, int amount);

    boolean doesUserExist(UUID uuid);

    void remove(UUID uuid);

}
