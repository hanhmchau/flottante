package services;

import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CostOfLivingService extends AbstractService {
    public CostOfLiving getCityCostOfLiving(int cityId) throws Exception {
        CostOfLiving c = new CostOfLiving();
        try {
            CategoryService categoryService = new CategoryService();
            List<Category> categories = categoryService.getCategories();
            categories.forEach(cat -> {
                c.getCategories().put(cat.getGroupName(), new ItemList());
            });
            String sql = "SELECT colc.name as 'name', cold.value as 'value', " +
                    "(SELECT name FROM CostOfLivingGroup WHERE id = colc.group_id) as 'group_name' " +
                    "FROM CostOfLivingDetail cold " +
                    "LEFT JOIN CostOfLivingCategory colc " +
                    "on cold.category_id = COLC.id WHERE cold.city_id = ?";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            psm.setInt(1, cityId);
            rs = psm.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                double value = rs.getDouble("value");
                String groupName = rs.getString("group_name");
                c.getCategories().get(groupName).getItems().add(new Item(name, value));
            }
        } finally {
            closeConnection();
        }
        return c;
    }

    public boolean insertCostOfLiving(List<City> cities, List<Category> categories) throws Exception {
        Map<String, Integer> map = categoryNameToIdMap(categories);
        try {
            String sql = "IF NOT EXISTS (SELECT * FROM CostOfLivingDetail d " +
                    "                   WHERE d.category_id = ? AND d.city_id = ?) " +
                    "   BEGIN " +
                    "       INSERT INTO CostOfLivingDetail(category_id, city_id, value)" +
                    "       VALUES (?,?,?) " +
                    "   END " +
                    "   ELSE " +
                    "   BEGIN " +
                    "       UPDATE CostOfLivingDetail SET value = ? WHERE category_id = ? AND city_id = ?" +
                    "   END";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            int counter = 0;
            int batchSize = 900;
            for (City city : cities) {
                CostOfLiving costOfLiving = city.getCostOfLiving();
                for (Map.Entry<String, ItemList> entry : costOfLiving.getCategories().entrySet()) {
                    ItemList itemList = entry.getValue();
                    for (Item item: itemList.getItems()) {
                        int itemCategoryId = map.get(item.getTitle());
                        psm.setInt(1, itemCategoryId);
                        psm.setInt(2, city.getId());
                        psm.setInt(3, itemCategoryId);
                        psm.setInt(4, city.getId());
                        psm.setDouble(5, item.getValue());
                        psm.setDouble(6, item.getValue());
                        psm.setInt(7, itemCategoryId);
                        psm.setInt(8, city.getId());
                        psm.addBatch();
                        counter++;
                        if (counter > batchSize) {
                            counter = 0;
                            psm.executeBatch();
                        }
                    }
                }
            }
            return true;
        } finally {
            closeConnection();
        }
    }

    private Map<String, Integer> categoryNameToIdMap(List<Category> categories) {
        Map<String, Integer> map = new HashMap<>();
        for (Category cat: categories) {
            map.putIfAbsent(cat.getName(), cat.getId());
        }
        return map;
    }
}
