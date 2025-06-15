package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.StockOutController;
import dao.ProductDAO;
import dao.StockOutDAO;
import model.Product;

import java.awt.*;
import java.util.Vector;

public class StockOutView extends JFrame {
    private JTable stockOutTable;
    private JButton addButton, editButton, deleteButton, refreshButton, backButton;
    private JButton searchButton, clearSearchButton;
    private JTextField searchField;
    private DefaultTableModel model;
    private DefaultTableModel originalModel; // menyimpan data awal
    private String role;

    public StockOutView(String role) {
        this.role = role;

        setTitle("Stock Out Management - Role: " + role);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 Top Panel: Back & Search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(10, 36, 106)); // Dark blue
        topPanel.setPreferredSize(new Dimension(getWidth(), 60));

        // Back Button (di pojok kiri)
        backButton = new JButton("← Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(17, 50, 120));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
        dispose();
        if ("admin".equals(role)) {
            new DashboardAdminView(role, "Admin");
        } else {
            new DashboardStaffView(role);
        }
        });

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setOpaque(false);
        backPanel.add(backButton);

        // Search Panel (di pojok kanan)
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        searchButton = new JButton("🔍 Search");
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(17, 50, 120));
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        clearSearchButton = new JButton("❌ Clear");
        clearSearchButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        clearSearchButton.setForeground(Color.WHITE);
        clearSearchButton.setBackground(new Color(17, 50, 120));
        clearSearchButton.setBorderPainted(false);
        clearSearchButton.setFocusPainted(false);
        clearSearchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Search by Product ID:", SwingConstants.RIGHT));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearSearchButton);

        topPanel.add(backPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 🔹 Table
        String[] columnNames = {"Stock Out ID", "Product ID", "Quantity", "Date"};
        model = new DefaultTableModel(columnNames, 0);
        originalModel = new DefaultTableModel(columnNames, 0); // Backup asli
        stockOutTable = new JTable(model);

        // Set table properties
        stockOutTable.setFillsViewportHeight(true);
        stockOutTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stockOutTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        stockOutTable.getTableHeader().setBackground(new Color(10, 36, 106));
        stockOutTable.getTableHeader().setForeground(Color.WHITE);
        stockOutTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(stockOutTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // 🔹 Bottom Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addButton = createStyledButton("➕ Add Stock Out");
        editButton = createStyledButton("✏️ Edit Stock Out");
        deleteButton = createStyledButton("🗑️ Delete Stock Out");
        refreshButton = createStyledButton("🔄 Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 🔹 Button actions
        addButton.addActionListener(e -> addStockOut());
        editButton.addActionListener(e -> editStockOut());
        deleteButton.addActionListener(e -> deleteStockOut());
        refreshButton.addActionListener(e -> refreshTable());

        searchButton.addActionListener(e -> searchStockOut());
        clearSearchButton.addActionListener(e -> clearSearch());

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(10, 36, 106));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addStockOut() {
    StockOutFormDialog dialog = new StockOutFormDialog(this, "Tambah Stock Out", true);
    if (dialog.isConfirmed()) {
        String productName = dialog.getSelectedProductName();
        int quantity = dialog.getQuantity();
        String destination = dialog.getDestination();

        Product product = new ProductDAO().getProductByName(productName);
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Produk tidak ditemukan.");
            return;
        }

        int productId = product.getProductID();
        boolean success = new StockOutController()
                .addStockOut(productId, quantity, 1, destination, dialog.getNotes());

        if (success) {
            refreshTable();
        }
    }
}

    private void editStockOut() {
        int selectedRow = stockOutTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            return;
        }

        String currentStockOutId = (String) model.getValueAt(selectedRow, 0);
        String currentProductId = (String) model.getValueAt(selectedRow, 1);
        String currentQuantity = (String) model.getValueAt(selectedRow, 2);
        String currentDate = (String) model.getValueAt(selectedRow, 3);

        JTextField stockOutIdField = new JTextField(currentStockOutId);
        JTextField productIdField = new JTextField(currentProductId);
        JTextField quantityField = new JTextField(currentQuantity);
        JTextField dateField = new JTextField(currentDate);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Stock Out ID:"));
        panel.add(stockOutIdField);
        panel.add(new JLabel("Product ID:"));
        panel.add(productIdField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Date:"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Stock Out",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            model.setValueAt(stockOutIdField.getText(), selectedRow, 0);
            model.setValueAt(productIdField.getText(), selectedRow, 1);
            model.setValueAt(quantityField.getText(), selectedRow, 2);
            model.setValueAt(dateField.getText(), selectedRow, 3);

            originalModel.setValueAt(stockOutIdField.getText(), selectedRow, 0);
            originalModel.setValueAt(productIdField.getText(), selectedRow, 1);
            originalModel.setValueAt(quantityField.getText(), selectedRow, 2);
            originalModel.setValueAt(dateField.getText(), selectedRow, 3);
        }
    }

    private void deleteStockOut() {
        int selectedRow = stockOutTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this stock out?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(selectedRow);
            originalModel.removeRow(selectedRow);
        }
    }

    public void refreshTable() {
    model.setRowCount(0);
    for (model.StockOut s : new StockOutDAO().getAllStockOut()) {
        model.addRow(new Object[]{
                s.getStockOutID(),
                s.getProductID(),
                s.getQuantity(),
                s.getDate(),
                s.getDestination(),
                s.getNotes()
        });
    }
}

    private void searchStockOut() {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) return;

        DefaultTableModel searchModel = new DefaultTableModel(
                new String[]{"Stock Out ID", "Product ID", "Quantity", "Date"}, 0);

        for (int i = 0; i < originalModel.getRowCount(); i++) {
            String productId = originalModel.getValueAt(i, 1).toString().toLowerCase();
            if (productId.contains(keyword)) {
                Vector<?> row = (Vector<?>) originalModel.getDataVector().get(i);
                searchModel.addRow(row);
            }
        }

        stockOutTable.setModel(searchModel);
        model = searchModel;
    }

    private void clearSearch() {
        stockOutTable.setModel(originalModel);
        model = originalModel;
        searchField.setText("");
    }

    // Getters if needed
    public JTable getStockOutTable() { return stockOutTable; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getRefreshButton() { return refreshButton; }
    public JButton getBackButton() { return backButton; }
}