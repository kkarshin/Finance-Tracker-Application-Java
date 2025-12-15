package database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerUsed {
        public boolean isAnswerUsed(String security_q) {
        String sql = "SELECT COUNT(*) FROM users WHERE security_q = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setString(1, security_q);
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
