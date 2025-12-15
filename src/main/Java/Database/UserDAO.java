package database;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author apayy
 */
import java.sql.*;
import model.User;

public class UserDAO {
    
    // CREATE

    public void addUser(String name, String password,String security_question, String security_q) {
        String sql = "INSERT INTO users (username,password,security_question,security_q) VALUES (?, ?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3,security_question);
            stmt.setString(4, security_q);
            
            stmt.executeUpdate();
            System.out.println("✅ User added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
public User getUser(String uname) {
    String sql = "SELECT * FROM users WHERE username = ?";
    try (Connection conn = DataBaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) { // ✅ Use PreparedStatement

        stmt.setString(1, uname); // ✅ Set value for ?

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int user_Id = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String securityAnswer = rs.getString("security_question");
                String securityQuestion = rs.getString("security_q");
            
                // Return a User object with the data
                return new User(username, password, securityQuestion, securityAnswer);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
        return null; // If not found       
    }

public void getAllUser() {
    String sql = "SELECT * FROM users ";
    try (Connection conn = DataBaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int user_Id = rs.getInt("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String securityAnswer = rs.getString("security_question");
                String securityQuestion = rs.getString("security_q");
                                System.out.println(user_Id + " | " + username + " | " + password + " | " + securityAnswer + " | " + securityQuestion);

            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
//    public boolean isUsernameUsed(String username) {
//        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
//        try (Connection conn = DataBaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//             stmt.setString(1, username);
//             try (ResultSet rs = stmt.executeQuery()) {
//                 if (rs.next()) {
//                // Get the count of matching usernames
//                int count = rs.getInt(1);
//                return count > 0;
//                        }
//}
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//    return false;
//}
//                        }
    

//     UPDATE
    public void updatePassword(String username, String password) {
        String sql = "UPDATE users SET password=? WHERE username=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, password);
            stmt.setString(2, username);
            stmt.executeUpdate();
            System.out.println("✅ User updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//     DELETE
    public void deleteUser(String username){
        String sql = "DELETE FROM users WHERE username=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,username);
            stmt.executeUpdate();
            System.out.println("✅ User deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


