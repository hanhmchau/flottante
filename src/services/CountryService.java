package services;

import models.Country;
import models.Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CountryService extends AbstractService {
    public void insertCountries(List<Country> countries) throws Exception {
        for (Country country : countries) {
            int id = insertCountryIfNotExists(country);
            if (id != -1) {
                country.setId(id);
            }
        }
    }

    public void insertCountryLanguages(List<Country> countries) throws Exception {
        try {
            String sql = "IF NOT EXISTS (SELECT * FROM CountryLanguages cl " +
                    "                   WHERE cl.country_id = ? AND cl.language_code = ?) " +
                    "   BEGIN " +
                    "       INSERT INTO CountryLanguages(country_id, language_code)" +
                    "       VALUES (?,?) " +
                    "   END";
            con = DBUtils.getMyConnection();
            psm = con.prepareStatement(sql);
            for (Country c: countries) {
                for (Language l: c.getLanguages()) {
                    psm.setInt(1, c.getId());
                    psm.setString(2, l.getCode());
                    psm.setInt(3, c.getId());
                    psm.setString(4, l.getCode());
                    psm.addBatch();
                }
            }
            psm.executeBatch();
        } finally {
            closeConnection();
        }
    }

    private int insertCountryIfNotExists(Country c) throws Exception {
        try {
            String sql = "IF NOT EXISTS (SELECT * FROM Country c " +
                    "                   WHERE c.name = ?) " +
                    "   BEGIN\n" +
                    "       INSERT INTO Country (name, minimum_wage) OUTPUT INSERTED.ID" +
                    "       VALUES (?,?) " +
                    "   END" +
                    "   ELSE" +
                    "       UPDATE Country SET minimum_wage = ? OUTPUT INSERTED.ID WHERE name = ?";
            con = DBUtils.getMyConnection();
            assert con != null;
            psm = con.prepareStatement(sql);
            psm.setString(1, c.getName());
            psm.setString(2, c.getName());
            psm.setDouble(3, c.getMinimumWage());
            psm.setDouble(4, c.getMinimumWage());
            psm.setString(5, c.getName());
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
