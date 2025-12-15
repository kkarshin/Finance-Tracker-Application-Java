/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
/**
 *
 * @author syeda
 */
// Class for expense, inherits from Transaction
public class Expense extends TransactionData {
    public Expense(LocalDate date, double amount, String category, String description) {
        super(date, amount, category, description);
    }
    
    public Expense(int trans_id, LocalDate date, double amount, String category, String description) {
        super(trans_id, date, amount, category, description);
    }

    @Override
    public String getType() {
        return "Expense";
    }
}

