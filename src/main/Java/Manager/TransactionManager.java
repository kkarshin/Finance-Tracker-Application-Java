/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * TransactionManager handles all transaction-related operations.
 * @author Guay Ern Xin
 */
package manager;

import model.TransactionData;
import model.Income;
import model.Expense;
import model.User;
//import manager.UserManager;
import java.util.ArrayList;
import java.util.List;
import database.TransactionDAO;
import database.GetTransaction;
//import java.time.LocalDate;


public class TransactionManager {
    private static final TransactionDAO tdao = new TransactionDAO(); 
    private static final GetTransaction trans = new GetTransaction();

    private static final List<TransactionData> transactions = new ArrayList<>();

    public static void loadTransactionFromDB(String username){
        GetTransaction getterTrans = new GetTransaction();
        transactions.clear();
        transactions.addAll(getterTrans.getTransactionsByUsername(username));
    }
    
    public static void addTransaction(TransactionData transaction) {
        User loggedInUser = UserManager.getInstance().getCurrentUser();
//        System.out.println(loggedInUser.getUsername());
        tdao.addTransactionForUser(
        loggedInUser.getUsername(),
        transaction.getType(),
        transaction.getDate(),
        transaction.getCategory(),
        transaction.getDescription(),
        transaction.getAmount()
    );
        
        transactions.add(transaction);
    }
    

    public static List<TransactionData> getTransactions() {
        return transactions;
    }

    public static void removeTransaction(int transId) {
    // Remove from DB
            TransactionDAO dao = new TransactionDAO();
            dao.deleteTransaction(transId);

    // Remove from memory
            transactions.removeIf(t -> t.getTransId() == transId);
}

    public static double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("Income")) // Calls Income.getType()
                .mapToDouble(TransactionData::getAmount)
                .sum();
    }

    public static double getTotalExpense() {
        return transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("Expense")) // Calls Expense.getType()
                .mapToDouble(TransactionData::getAmount)
                .sum();
    }

    public static double getNetCashFlow() {
        return getTotalIncome() - getTotalExpense();
    }
    
    public static void reloadTransactions() {
        User loggedInUser = UserManager.getInstance().getCurrentUser(); // get current user
        if (loggedInUser != null) {
        transactions.clear();
        transactions.addAll(trans.getTransactionsByUsername(loggedInUser.getUsername())); // fetch from DB
    }
}
    // ---- Validation ----
    /**
     * Validates an amount string and returns it as a double if valid.
     * @param amountText
     * @return 
     * @throws IllegalArgumentException if invalid.
     */
    public static double validateAmount(String amountText) {
        if (amountText == null) {
            throw new IllegalArgumentException("Amount cannot be null.");
        }

        amountText = amountText.trim();

        if (!amountText.matches("\\d+(\\.\\d{1,2})?")) {
            throw new IllegalArgumentException("Amount must be a valid number with up to 2 decimal places.");
        }

        double amount = Double.parseDouble(amountText);
        if (amount == 0.00) {
            throw new IllegalArgumentException("Amount cannot be 0.00.");
        }

        return amount;
    }
}

