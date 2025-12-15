/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author apayy
 */
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.TransactionData;
import model.Income;
import model.Expense;

public class GetTransaction {
        public List<TransactionData> getTransactionsByUsername(String username) {
        List<TransactionData> transactions = new ArrayList<>();

        String getUserIdSQL = "SELECT user_id FROM users WHERE username = ?";
        String getTransactionsSQL = "SELECT type, trans_date, amount, category, description, trans_id FROM transactions WHERE user_id = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement userStmt = conn.prepareStatement(getUserIdSQL)) {

            // Step 1: Get the user_id
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                int userId = userRs.getInt("user_id");

                // Step 2: Get transactions for that user_id
                try (PreparedStatement transStmt = conn.prepareStatement(getTransactionsSQL)) {
                    transStmt.setInt(1, userId);
                    ResultSet rs = transStmt.executeQuery();

                    while (rs.next()) {
                        int id = rs.getInt("trans_id");
                        String type = rs.getString("type");
                        LocalDate date = rs.getDate("trans_date").toLocalDate();
                        double amount = rs.getDouble("amount");
                        String category = rs.getString("category");
                        String description = rs.getString("description");
//                        int userId = rs.getInt("user_id");

                        TransactionData transaction;

                        if ("Income".equalsIgnoreCase(type)) {
                            transaction = new Income(id, date, amount, category, description);
                        } else if ("Expense".equalsIgnoreCase(type)) {
                            transaction = new Expense(id, date, amount, category, description);
                        } else {
                            // fallback if type is unknown
                            continue;
    }
                    

                                transactions.add(transaction);

//                        // Step 3: Map to model and add to list
                        System.out.println(
                        rs.getString("type") + " - " +
                        rs.getDate("trans_date").toLocalDate() + " - " +
                        rs.getInt("amount") + " - " +
                        rs.getString("category") + " - " +
                        rs.getString("description") + " - " +
                        rs.getInt("trans_id")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
}

