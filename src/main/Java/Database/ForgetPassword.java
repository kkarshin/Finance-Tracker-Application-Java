/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author apayy
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgetPassword {
    public boolean resetPassword(String username, String securityAnswer, String newPassword) {
    String checkSql = "SELECT * FROM users WHERE username = ? AND security_q = ?";
    String updateSql = "UPDATE users SET password = ? WHERE username = ?";

    try (Connection conn = DataBaseConnection.getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

        // Step 1: Check if the security answer matches
        checkStmt.setString(1, username);
        checkStmt.setString(2, securityAnswer);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            // Step 2: Update the password
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, newPassword);
                updateStmt.setString(2, username);
                updateStmt.executeUpdate();
                return true; // password updated
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // wrong answer or user not found
}

}
