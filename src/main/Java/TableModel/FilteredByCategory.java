/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tableModel;

import model.Category;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import model.TransactionData;

/**
 *
 * @author Hp
 */
public class FilteredByCategory extends AbstractTableModel {

    private final String[] columnNames = {"Category", "Total", "Percentage"};
    private final Map<Category, Double> categoryTotals = new EnumMap<>(Category.class);
    private double totalAmount;

    public FilteredByCategory() {
        for (Category c : Category.values()) {
            categoryTotals.put(c, 0.0);
        }
    }

    // Refresh based on TransactionData list
    public void refresh(List<TransactionData> transactions) {
        // Reset totals
        for (Category c : Category.values()) {
            categoryTotals.put(c, 0.0);
        }
        totalAmount = 0;

        // Calculate totals
        for (TransactionData t : transactions) {
            Category cat ;
            try {
                cat = Category.valueOf(t.getCategory());
            } catch (IllegalArgumentException e) {
                cat = Category.Others; // fallback if unknown category
            }
            double current = categoryTotals.getOrDefault(cat, 0.0);
            categoryTotals.put(cat, current + t.getAmount());
            totalAmount += t.getAmount();
        }

        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return Category.values().length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Category category = Category.values()[rowIndex];
        switch (columnIndex) {
            case 0:
                return category.name();
            case 1:
                return String.format("$ %.2f",(categoryTotals.get(category)));
            case 2:
                if (totalAmount > 0) {
                    return String.format("%.2f%%", (categoryTotals.get(category) / totalAmount) * 100);
                } else {
                    return "0.00%";
                }
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

