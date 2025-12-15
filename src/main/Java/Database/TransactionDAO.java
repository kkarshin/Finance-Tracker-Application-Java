package database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import model.TransactionData;
import model.Income;
import model.Expense;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author apayy
 */
public class TransactionDAO {
       // CREATE
//    public void addTransaction(String trans_date, float amount, String category, String description, int id) {
//        String sql = "INSERT INTO transactions (trans_date,amount,category,description,user_id) VALUES (?, ?, ?, ?, ?)";
//        try (Connection conn = DataBaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, trans_date);
//            stmt.setFloat(2, amount);
//            stmt.setString(3, category);
//            stmt.setString(4, description);
//            stmt.setInt(5,id);
////            stmt.setString(6,username);
//            stmt.executeUpdate();
//            System.out.println("✅ User added!");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    public void addTransactionForUser(String username, String type, LocalDate date, String category, String description, double amount) {
    String getUserIdSQL = "SELECT user_id FROM users WHERE username = ?";
    String insertSQL = "INSERT INTO transactions (type, user_id, trans_date, category, description, amount) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DataBaseConnection.getConnection();
         PreparedStatement getUserStmt = conn.prepareStatement(getUserIdSQL);
         PreparedStatement insertStmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

        // Get user_id
        getUserStmt.setString(1, username);
        ResultSet rs = getUserStmt.executeQuery();
        if (rs.next()) {
            int userId = rs.getInt("user_id");

            // Insert transaction
            insertStmt.setString(1, type);
            insertStmt.setInt(2, userId);
            insertStmt.setDate(3, java.sql.Date.valueOf(date));
            insertStmt.setString(4, category);
            insertStmt.setString(5, description);
            insertStmt.setDouble(6, amount);
            insertStmt.executeUpdate();
            
            // 👇 Get the auto-generated transaction ID
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int transId = generatedKeys.getInt(1);
                
                TransactionData transaction;
                if ("Income".equalsIgnoreCase(type)) {
                    transaction = new Income(transId, date, amount, category, description);
                } else if ("Expense".equalsIgnoreCase(type)) {
                    transaction = new Expense(transId, date, amount, category, description);
                } else {
                    // default fallback if type is invalid
                    throw new IllegalArgumentException("Unknown transaction type: " + type);
                }
//                TransactionData transaction = new SomeConcreteTransactionClass(transId, date, amount, category, description);

                System.out.println("Transaction added successfully for " + username);
            }
        } else {
            System.out.println("User not found: " + username);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // READ
    public List<TransactionData> getAllTransactions() {
        List<TransactionData> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String type = rs.getString("type");
                LocalDate date = rs.getDate("trans_date").toLocalDate();
                double amount = rs.getDouble("amount");
                String category = rs.getString("category");
                String description = rs.getString("description");
                int userId = rs.getInt("user_id");
                int transId = rs.getInt("trans_id");

                TransactionData transaction;
                if ("Income".equalsIgnoreCase(type)) {
                    transaction = new Income(date, amount, category, description);
                } else {
                    transaction = new Expense(date, amount, category, description);
                }

                transaction.setTransId(transId);
//                transaction.setUserId(userId);

                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
//    public void getAllTransactions() {
//        String sql = "SELECT * FROM transactions";
//        try (Connection conn = DataBaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
////                System.out.println(
//                                   rs.getString("type");
//                                   rs.getDate("trans_date").toLocalDate();
//                                   rs.getString("amount");
//                                   rs.getString("category");
//                                   rs.getString("description");
//                                   rs.getInt("user_id");
//                                   rs.getInt("trans_id");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    
    public void getTransactions(String username){
            String getUserIdSQL = "SELECT user_id FROM users WHERE username = ?";
            String sql = "SELECT * FROM transactions WHERE user_id=?";
            try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             while (rs.next()) {

                                   rs.getString("type");
                                   rs.getDate("trans_date").toLocalDate();
                                   rs.getInt("amount");
                                   rs.getString("category");
                                   rs.getString("description");
                                   rs.getInt("user_id");
                                int trans_id = rs.getInt("trans_id");
                                  System.out.println(trans_id);
          
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
    }
    
//        transaction.getType(),
//        transaction.getDate(),
//        transaction.getCategory(),
//        transaction.getDescription(),
//        transaction.getAmount()

    // UPDATE
    public void updateTransaction(int trans_id, String type, LocalDate trans_date, double amount, String category, String description) {
//        String getUserIdSQL = "SELECT user_id FROM users WHERE username = ?";
        String sql = "UPDATE transactions SET type=?, trans_date=?, amount=?, category=?, description=? WHERE trans_id=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, type);
            stmt.setDate(2, java.sql.Date.valueOf(trans_date));
            stmt.setDouble(3, amount);
            stmt.setString(4, category);
            stmt.setString(5, description);
            stmt.setInt(6, trans_id);
            stmt.executeUpdate();
            System.out.println("✅ Transaction updated!");
            System.out.println("Updating transaction with trans_id = " + trans_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteTransaction(int trans_id) {
        String sql = "DELETE FROM transactions WHERE trans_id=?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, trans_id);
            stmt.executeUpdate();
            System.out.println("✅ User deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
