package services;

import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CategoryService extends AbstractService {
    public boolean updateCategory(int id, double weight) throws Exception {
        try {
            String sql = "UPDATE CostOfLivingCategory SET weight = ? WHERE id = ?";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            psm.setDouble(1, weight);
            psm.setInt(2, id);
            return psm.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
    }

    public List<Category> getCategories() throws Exception {
        List<Category> list = new ArrayList<>();
        try {
            String sql = "SELECT colc.id, colc.name, weight, colg.id as 'group_id', colg.name as 'group_name' FROM CostOfLivingCategory colc " +
                    "LEFT JOIN CostOfLivingGroup colg " +
                    "on colc.group_id = colg.id";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            rs = psm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int groupId = rs.getInt("group_id");
                String groupName = rs.getString("group_name");
                double weight = rs.getDouble("weight");
                Category c = new Category(id, name, weight);
                c.setGroupId(groupId);
                c.setGroupName(groupName);
                list.add(c);
            }
        } finally {
            closeConnection();
        }
        return list;
    }

    public Map<String, Integer> insertGroups(Set<String> groups) throws Exception {
        Map<String, Integer> map = new HashMap<>();
        try {
            String sql = "IF NOT EXISTS (SELECT * FROM CostOfLivingGroup g " +
                    "                   WHERE g.name = ?) " +
                    "   BEGIN " +
                    "       INSERT INTO CostOfLivingGroup(name) OUTPUT INSERTED.id" +
                    "       VALUES (?) " +
                    "   END " +
                    "   ELSE " +
                    "   BEGIN " +
                    "       SELECT id FROM CostOfLivingGroup WHERE name = ?" +
                    "   END";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            for (String group : groups) {
                psm.setString(1, group);
                psm.setString(2, group);
                psm.setString(3, group);
                boolean result = psm.execute();
                if (result) {
                    rs = psm.getResultSet();
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        map.put(group, id);
                    }
                }
            }
        } finally {
            closeConnection();
        }
        return map;
    }

    public List<Category> insertCategories(Set<Category> categories, Map<String, Integer> allGroups) throws Exception {
        List<Category> list = new ArrayList<>();
        Set<String> categoryNames = new HashSet<>();
        Set<Category> filteredCategories = new HashSet<>();
        categories.forEach(cat -> {
            if (!categoryNames.contains(cat.getName())) {
                filteredCategories.add(cat);
                categoryNames.add(cat.getName());
            }
        });
        try {
            String sql = "INSERT INTO CostOfLivingCategory (name, weight, group_id) " +
                    "OUTPUT INSERTED.ID, INSERTED.name VALUES (?, ?, ?)";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            for (Category cat: filteredCategories) {
                psm.setString(1, cat.getName());
                psm.setDouble(2, -1);
                psm.setInt(3, allGroups.get(cat.getGroupName()));
                boolean result = psm.execute();
                if (result) {
                    ResultSet res = psm.getResultSet();
                    while (res.next()) {
                        int id = res.getInt("id");
                        String name = res.getString("name");
                        Category newCat = new Category(id, name, 1.0);
                        list.add(newCat);
                    }
                }
            }
        } finally {
            closeConnection();
        }
        return list;
    }
}
