package services;

import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CityService extends AbstractService {
    public List<City> getCities() throws Exception {
        List<City> cities = new ArrayList<>();
        try {
            String sql = "SELECT id, name, (SELECT image FROM CityImages WHERE id = " +
                    "       (SELECT max(id) FROM CityImages WHERE city_id = c.id) ) as coverImage " +
                    "FROM City c ORDER BY name";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            rs = psm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String coverImage = rs.getString("coverImage");
                cities.add(new City(id, name, coverImage));
            }
        } finally {
            closeConnection();
        }
        return cities;
    }

    public List<String> getImages(int cityId) throws Exception {
        List<String> images = new ArrayList<>();
        try {
            String sql = "SELECT image FROM CityImages WHERE city_id = ?";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            psm.setInt(1, cityId);
            rs = psm.executeQuery();
            while (rs.next()) {
                String image = rs.getString("image");
                images.add(image);
            }
        } finally {
            closeConnection();
        }
        return images;
    }

    public City getCity(int id) throws Exception {
        LanguageService languageService = new LanguageService();
        CostOfLivingService costOfLivingService = new CostOfLivingService();
        try {
            String sql = "SELECT name, country_id, (SELECT name from Country WHERE id = c.country_id) as country_name," +
                    "population, cost_of_living_ranking, safety_ranking " +
                    "FROM City c WHERE id = ?";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            psm.setInt(1, id);
            rs = psm.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String countryName = rs.getString("country_name");
                int countryId = rs.getInt("country_id");
                int population = rs.getInt("population");
                int costOfLivingRanking = rs.getInt("cost_of_living_ranking");
                int safetyRanking = rs.getInt("safety_ranking");
                City c = new City(id, name);
                c.setCountry(new Country(countryId, countryName));
                c.setCostOfLivingRanking(costOfLivingRanking);
                c.setPopulation(population);
                c.setSafetyRanking(safetyRanking);
                c.setCostOfLiving(costOfLivingService.getCityCostOfLiving(id));
                c.setLanguages(languageService.getCityLanguages(id).stream().map(Language::getName).collect(Collectors.toList()));
                c.setImages(getImages(id));
                return c;
            }
        } finally {
            closeConnection();
        }
        return null;
    }

    public List<City> filterCities(CityFilter filter, int page, int pageSize) throws Exception {
        List<City> cities = new ArrayList<>();
        String query = "WHERE ";
        List<String> languages = filter.getLanguages();
        if (!languages.isEmpty()) {
            StringBuilder languageQuery = new StringBuilder(" country_id IN " +
                    "(SELECT country_id FROM CountryLanguages cl WHERE cl.language_code IN (");
            for (int i = 0; i < languages.size() - 1; i++) {
                languageQuery.append("'").append(languages.get(i)).append("',");
            }
            languageQuery.append("'").append(languages.get(languages.size() - 1)).append("'))");
            query += languageQuery.toString();
            query += " AND ";
        }
        if (filter.getPopulationMax() > 0) {
            query += " population < " + filter.getPopulationMax() + " AND ";
        }
        if (filter.getPopulationMin() > 0) {
            query += " population > " + filter.getPopulationMin() + " AND ";
        }
        query += " 1 = 1";
        int offset = Math.max((page - 1) * pageSize, 0);

        try {
            String sql = String.format("SELECT id, name, safety_ranking * %d + cost_of_living_ranking * %d as score " +
                    "FROM City c %s ORDER BY score DESC OFFSET %d ROWS FETCH NEXT %d ROWS ONLY ",
                    filter.getSafetyIndexPriority(), filter.getCostOfLivingIndexPriority(), query, offset, pageSize);
            System.out.println(sql);
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            rs = psm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int score = rs.getInt("score");
                City c = new City(id, name);
                c.setRankingScore(score);
                cities.add(c);
            }
        } finally {
            closeConnection();
        }
        return cities;
    }

    public void insertCities(List<City> cities) throws Exception {
        for (City city : cities) {
            int id = insertCityIfNotExists(city);
            if (id != -1) {
                city.setId(id);
            }
            insertCityImages(city.getId(), city.getImages());
        }
    }

    private boolean insertCityImages(int cityId, List<String> images) throws Exception {
        if (images.isEmpty()) {
            return true;
        }
        try {
            String sql = "IF NOT EXISTS (SELECT * FROM CityImages c " +
                    "                   WHERE c.city_id = ? AND c.image = ?) " +
                    "   BEGIN " +
                    "       INSERT INTO CityImages(city_id, image)" +
                    "       VALUES (?,?) " +
                    "   END";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            for (String img : images) {
                psm.setInt(1, cityId);
                psm.setString(2, img);
                psm.setInt(3, cityId);
                psm.setString(4, img);
                psm.addBatch();
            }
            return psm.executeBatch().length > 0;
        } finally {
            closeConnection();
        }
    }

    private int insertCityIfNotExists(City c) throws Exception {
        try {
            String sql = "IF NOT EXISTS (SELECT * FROM City c " +
                    "                   WHERE c.name = ?) " +
                    "   BEGIN\n" +
                    "       INSERT INTO City (name, country_id, population, cost_of_living_ranking, safety_ranking) OUTPUT INSERTED.ID" +
                    "       VALUES (?,?,?,?,?) " +
                    "   END " +
                    "   ELSE " +
                    "       UPDATE City SET country_id = ?, population = ?, cost_of_living_ranking = ?, safety_ranking = ? OUTPUT INSERTED.ID" +
                    "       WHERE name = ?";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            psm.setString(1, c.getName());
            psm.setString(2, c.getName());
            psm.setInt(3, c.getCountry().getId());
            psm.setInt(4, c.getPopulation());
            psm.setInt(5, c.getCostOfLivingRanking());
            psm.setInt(6, c.getSafetyRanking());
            psm.setInt(7, c.getCountry().getId());
            psm.setInt(8, c.getPopulation());
            psm.setInt(9, c.getCostOfLivingRanking());
            psm.setInt(10, c.getSafetyRanking());
            psm.setString(11, c.getName());
            boolean hasResultSet = psm.execute();
            if (hasResultSet) {
                ResultSet res = psm.getResultSet();
                if (res.next()) {
                    return res.getInt(1);
                }
            }
        } finally {
            closeConnection();
        }
        return -1;
    }
}
