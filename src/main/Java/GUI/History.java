/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * History screen for Budget Buddy: shows all transactions with Edit/Delete actions.
 * @author Guay Ern Xin
 */

package gui;

import model.TransactionData;
import model.Expense;
import model.Income;
import manager.TransactionManager;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.table.TableCellRenderer;
import database.TransactionDAO;
import database.GetTransaction;

public class History extends JFrame {
    private final JTable transactionTable;
    private final JButton backButton;
    private final JButton filterButton;
    private final DefaultTableModel tableModel;
    public static History instance;
    private final static TransactionDAO trans = new TransactionDAO();
    private final static GetTransaction tdao = new GetTransaction();

    public History() {
        instance = this;
        setTitle("Transaction History");
        setSize(900, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // ✅ Keep real DB trans_id as hidden column
        String[] columnNames = {"DB_ID", "ID", "Type", "Date", "Amount", "Category", "Description", "Edit", "Delete"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7 || column == 8; // Only Edit/Delete
            }
        };

        transactionTable = new JTable(tableModel);
        transactionTable.setRowHeight(30);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setAutoCreateRowSorter(true);

        // Hide the DB_ID column from the user
        transactionTable.removeColumn(transactionTable.getColumnModel().getColumn(0));

        // Button renderers
        transactionTable.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        transactionTable.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), true));

        transactionTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        transactionTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), false));

        JScrollPane scrollPane = new JScrollPane(transactionTable);

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose();
            new Dashboard().setVisible(true);
        });

        filterButton = new JButton("Filter by Category");
        filterButton.addActionListener(e -> {
            dispose();
            new tableModel.MainFrame().setVisible(true);
        });

        panel.add(scrollPane, BorderLayout.CENTER);
        buttonPanel.add(filterButton);
        buttonPanel.add(backButton);

        add(panel);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        updateHistory();
        setVisible(true);
    }

    public static void refreshData() {
        if (instance != null) {
            instance.updateHistory();
        }
    }

    /** Reloads the table with transactions from DB/Manager. */
    public void updateHistory() {
        tableModel.setRowCount(0);
        List<TransactionData> transactions = TransactionManager.getTransactions();
        int rowNum = 1;
        for (TransactionData t : transactions) {
            String type = t.getType().trim().toLowerCase();
            String sign = type.equals("income") ? "+" : "-";

            tableModel.addRow(new Object[]{
                t.getTransId(), // ✅ hidden real DB ID
                rowNum++,       // visible row number
                t.getType(),
                t.getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                String.format("%s$ %.2f", sign, t.getAmount()),
                t.getCategory(),
                t.getDescription(),
                "Edit",
                "Delete"
            });
        }
    }

    /** Renderer for Edit/Delete buttons. */
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private String label;
        private final boolean isEditButton;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox, boolean isEditButton) {
            super(checkBox);
            this.isEditButton = isEditButton;
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(e -> {
                if (transactionTable.isEditing()) {
                    transactionTable.getCellEditor().stopCellEditing();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            selectedRow = row;
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            return button;
        }

        @Override
        public Object getCellEditorValue() {

            SwingUtilities.invokeLater(() -> {
                if (isEditButton) {
                   editTransaction(selectedRow);
                 } else {
                   deleteTransaction(selectedRow);
            }
        });
          return label;
    }
    }

    private void editTransaction(int row) {
        List<TransactionData> transactions = TransactionManager.getTransactions();
        if (row < 0) return;

        int modelRow = transactionTable.convertRowIndexToModel(row);
        if (modelRow >= transactions.size()) return;

        TransactionData t = transactions.get(modelRow);
        int transId = t.getTransId(); // ✅ always use DB ID

        boolean validInput = false;

        while (!validInput) {
            JComboBox<String> typeField = new JComboBox<>(new String[]{"Income", "Expense"});
            typeField.setSelectedItem(t.getType());

            JTextField dateField = new JTextField(t.getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            JTextField amountField = new JTextField(String.valueOf(t.getAmount()));
            JComboBox<String> categoryField = new JComboBox<>(new String[]{"Food", "Fashion", "Health", "Transport", "Salary", "Allowance", "Others"});
            categoryField.setSelectedItem(t.getCategory());
            JTextField descriptionField = new JTextField(t.getDescription());

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Type:"));
            panel.add(typeField);
            panel.add(new JLabel("Date (YYYY/MM/DD):"));
            panel.add(dateField);
            panel.add(new JLabel("Amount:"));
            panel.add(amountField);
            panel.add(new JLabel("Category:"));
            panel.add(categoryField);
            panel.add(new JLabel("Description:"));
            panel.add(descriptionField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Edit Transaction",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) break;

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                java.time.LocalDate newDate = java.time.LocalDate.parse(dateField.getText().trim(), formatter);
                if (newDate.isAfter(java.time.LocalDate.now())) {
                    JOptionPane.showMessageDialog(this,
                            "Date cannot be in the future.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                String newType = (String) typeField.getSelectedItem();
                double newAmount = TransactionManager.validateAmount(amountField.getText());
                String newCategory = (String) categoryField.getSelectedItem();
                String newDescription = descriptionField.getText().trim();

                TransactionData updatedTransaction;
                if (newType.equalsIgnoreCase("Income")) {
                    updatedTransaction = new Income(transId, newDate, newAmount, newCategory, newDescription);
                } else {
                    updatedTransaction = new Expense(transId, newDate, newAmount, newCategory, newDescription);
                }

                // Update DB
                TransactionDAO dao = new TransactionDAO();
                dao.updateTransaction(transId, newType, newDate, newAmount, newCategory, newDescription);

                // Update in-memory list
                transactions.set(modelRow, updatedTransaction);

                TransactionManager.reloadTransactions();
                updateHistory();
                Dashboard.refreshData();
                validInput = true;
                
                System.out.println("Editing row: " + modelRow + " | DB transId = " + transId);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteTransaction(int row) {
        List<TransactionData> transactions = TransactionManager.getTransactions();
        if (row < 0) return;

        int modelRow = transactionTable.convertRowIndexToModel(row);
        if (modelRow >= transactions.size()) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this transaction?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            TransactionData transaction = transactions.get(modelRow);
            int transId = transaction.getTransId(); // ✅ real DB ID

            TransactionDAO dao = new TransactionDAO();
            dao.deleteTransaction(transId);

            transactions.remove(modelRow);

            TransactionManager.reloadTransactions();
            updateHistory();
            Dashboard.refreshData();
        }
    }
}




