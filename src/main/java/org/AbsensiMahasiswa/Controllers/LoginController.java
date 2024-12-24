package org.AbsensiMahasiswa.Controllers;

import org.AbsensiMahasiswa.Utils.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    private DatabaseHelper databaseHelper;

    public LoginController(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Authenticate admin based on username and password and return the admin ID.
     *
     * @param username Admin username
     * @param password Admin password
     * @return admin ID if authentication is successful, -1 if authentication fails or an error occurs.
     */
    public int authenticate(String username, String password) {
        String query = "SELECT admin_id FROM Admin WHERE Username = ? AND Password = ?";

        try (Connection connection = databaseHelper.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("admin_id");  // Return the admin ID if login is successful
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        // Return -1 if authentication fails or an error occurs
        return -1;
    }
}
