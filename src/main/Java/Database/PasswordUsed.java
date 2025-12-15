/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author apayy
 */
public class PasswordUsed {
        public boolean isPasswordUsed(String password) {
        String sql = "SELECT COUNT(*) FROM users WHERE password = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setString(1, password);
             try (ResultSet rs = stmt.executeQuery()) {
                 if (rs.next()) {
                // Get the count of matching usernames
                int count = rs.getInt(1);
                return count > 0;
                        }
}
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
}
