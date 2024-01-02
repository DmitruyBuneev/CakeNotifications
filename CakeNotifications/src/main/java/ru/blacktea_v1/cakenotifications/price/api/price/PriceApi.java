package ru.blacktea_v1.cakenotifications.price.api.price;

import ru.blacktea_v1.cakenotifications.CakeNotifications;
import ru.blacktea_v1.cakenotifications.databases.MySql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author blacktea_v1
 * @date ⭐ 31.12.2023 | 14:57⭐
 */
public class PriceApi implements IPriceApi {

    private MySql mySQL;

    @Override
    public Integer getPrice(UUID uuid) {
        String qry = "SELECT price FROM priceapi WHERE uuid=?";
        try (ResultSet rs = mySQL.query(qry, uuid.toString())) {
            if (rs.next()) {
                return rs.getInt("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void addPrice(UUID uuid, int amount) {
        mySQL.update("UPDATE priceapi SET price=? WHERE uuid=?", getPrice(uuid) + amount, uuid.toString());
    }

    @Override
    public void removePrice(UUID uuid, int amount) {
        int currentCoins = getPrice(uuid);

        if (currentCoins >= amount) {
            mySQL.update("UPDATE priceapi SET price=? WHERE uuid=?", currentCoins - amount, uuid.toString());
        }
    }

    @Override
    public void setPrice(UUID uuid, int amount) {
        mySQL.update("UPDATE priceapi SET price=? WHERE uuid=?", amount, uuid.toString());
    }

    public void initPlayer(UUID uuid) {
        mySQL.update("INSERT INTO priceapi (uuid, price) VALUES (?,?)", uuid.toString(), 0);
    }

    @Override
    public boolean doesUserExist(UUID uuid) {
        String qry = "SELECT count(*) AS count FROM priceapi WHERE uuid=?";
        ResultSet rs = mySQL.query(qry, uuid.toString());
        try {
            if (rs.next()) {
                return rs.getInt("count") != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public void remove(UUID uuid) {
        mySQL.update("DELETE FROM priceapi WHERE uuid =?;", uuid.toString());
    }

    public void createTables() {
        this.mySQL = CakeNotifications.getInstance().getMySQL();
        mySQL.update("CREATE TABLE IF NOT EXISTS priceapi (uuid VARCHAR(36), price INT(35))");
    }
}
