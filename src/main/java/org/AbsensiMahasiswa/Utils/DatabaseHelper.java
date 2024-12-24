package org.AbsensiMahasiswa.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

public class DatabaseHelper {
    private String connectionString;

    // Constructor 
    public DatabaseHelper(String server, String database, String userId, String password) {
        connectionString = "jdbc:mysql://" + server + "/" + database + "?user=" + userId + "&password=" + password;
    }

    // Method 
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }

    // Method untuk query yang tidak menghasilkan data (INSERT, UPDATE, DELETE)
    public void executeNonQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, "Error: {0}", ex.getMessage());
        }
    }

    // Method untuk query yang menghasilkan data (SELECT)
    public CachedRowSet executeQuery(String query) {
        CachedRowSet crs = null;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(resultSet);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, "Error: {0}", ex.getMessage());
        }
        return crs;
    }
}
