package services;

import models.City;
import models.Country;
import models.Language;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LanguageService extends AbstractService {
    public Set<Language> getLanguages(List<Country> countries) {
        return countries.stream().flatMap(c -> c.getLanguages().stream()).collect(Collectors.toSet());
    }

    public List<Language> getCityLanguages(int cityId) throws Exception {
        List<Language> languages = new ArrayList<>();
        try {
            String sql = "SELECT code, name FROM Language l WHERE l.code IN " +
                    "(SELECT language_code FROM CountryLanguages WHERE country_id = (SELECT country_id FROM City WHERE id = ?)) " +
                    "ORDER BY name";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            psm.setInt(1, cityId);
            rs = psm.executeQuery();
            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                languages.add(new Language(code, name));
            }
        } finally {
            closeConnection();
        }
        return languages;
    }

    public List<Language> getLanguages() throws Exception {
        List<Language> languages = new ArrayList<>();
        try {
            String sql = "SELECT code, name FROM Language ORDER BY name";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            rs = psm.executeQuery();
            while (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("name");
                languages.add(new Language(code, name));
            }
        } finally {
            closeConnection();
        }
        return languages;
    }

    public boolean insertLanguages(Set<Language> languages) throws Exception {
        try {
            String sql = "IF NOT EXISTS (SELECT * FROM Language l " +
                    "                   WHERE l.code = ?) " +
                    "   BEGIN\n" +
                    "       INSERT INTO Language (code, name) " +
                    "       VALUES (?,?) " +
                    "   END";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            for (Language lang : languages) {
                psm.setString(1, lang.getCode());
                psm.setString(2, lang.getCode());
                psm.setString(3, lang.getName());
                psm.addBatch();
            }
            int[] result = psm.executeBatch();
            return result.length > 0;
        } finally {
            closeConnection();
        }
    }
}
