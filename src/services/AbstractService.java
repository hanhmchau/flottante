package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class AbstractService {
    Connection con;
    PreparedStatement psm;
    ResultSet rs;

    public void closeConnection() throws Exception {
        //right order!
        if (rs != null) {
            rs.close();
        }
        if (psm != null) {
            psm.close();
        }
        if (con != null) {
            con.close();
        }
    }
}
