/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * TransactionData is the simple data holder used by the in-memory app today.
 * @author Guay Ern Xin
 */
package model;

import java.time.LocalDate;

public abstract class TransactionData {
    private int trans_id;
    private LocalDate date;
    private double amount;
    private String category;
    private String description;

    // Existing constructor (for new transaction)
    public TransactionData(LocalDate date, double amount, String category, String description) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    // Overloaded constructor (for when we know the ID, e.g. after DB insert or fetch)
    public TransactionData(int trans_id, LocalDate date, double amount, String category, String description) {
        this.trans_id = trans_id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }
    
    // Abstract method to force subclasses to define their type
    public abstract String getType();
    
    public int getTransId(){
        return trans_id;
    }
    
    public void setTransId(int trans_id){
        this.trans_id = trans_id;
    }
    
    public LocalDate getDate() {
        return date;
    }
    public double getAmount() {
        return amount;
    }
    public String getCategory () {
        return category;
    }
    public String getDescription() {
        return description;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}

