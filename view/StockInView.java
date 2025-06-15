package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.StockInController;
import dao.ProductDAO;
import dao.StockInDAO;
import model.Product;

import java.awt.*;
import java.util.Vector;

public class StockInView extends JFrame {
    private JTable stockInTable;
    private JButton addButton, editButton, deleteButton, refreshButton, backButton;
    private JButton searchButton, clearSearchButton;
    private JTextField searchField;
    private DefaultTableModel model;
    private DefaultTableModel originalModel; // menyimpan data awal
    private String role;

    public StockInView(String role) {
        this.role = role;

        setTitle("Stock In Management - Role: " + role);
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
        String[] columnNames = {"Stock In ID", "Product ID", "Quantity", "Date"};
        model = new DefaultTableModel(columnNames, 0);
        originalModel = new DefaultTableModel(columnNames, 0); // Backup asli
        stockInTable = new JTable(model);

        // Set table properties
        stockInTable.setFillsViewportHeight(true);
        stockInTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stockInTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        stockInTable.getTableHeader().setBackground(new Color(10, 36, 106));
        stockInTable.getTableHeader().setForeground(Color.WHITE);
        stockInTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(stockInTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // 🔹 Bottom Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addButton = createStyledButton("➕ Add Stock In");
        editButton = createStyledButton("✏️ Edit Stock In");
        deleteButton = createStyledButton("🗑️ Delete Stock In");
        refreshButton = createStyledButton("🔄 Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 🔹 Button actions
        addButton.addActionListener(e -> addStockIn());
        editButton.addActionListener(e -> editStockIn());
        deleteButton.addActionListener(e -> deleteStockIn());
        refreshButton.addActionListener(e -> refreshTable());

        searchButton.addActionListener(e -> searchStockIn());
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

    private void addStockIn() {
    StockInFormDialog dialog = new StockInFormDialog(this, "Tambah Stock In", true);
    if (dialog.isConfirmed()) {
        String productName = dialog.getSelectedProductName();
        int quantity = dialog.getQuantity();
        String supplier = dialog.getSupplierName();

        // Ambil productID dari nama produk
        Product product = new ProductDAO().getProductByName(productName);
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Produk tidak ditemukan.");
            return;
        }

        int productId = product.getProductID();
        boolean success = new StockInController()
                .addStockIn(productId, quantity, 1, supplier, dialog.getNotes());

        if (success) {
            refreshTable();
        }
    }
}

    private void editStockIn() {
        int selectedRow = stockInTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            return;
        }

        String currentStockInId = (String) model.getValueAt(selectedRow, 0);
        String currentProductId = (String) model.getValueAt(selectedRow, 1);
        String currentQuantity = (String) model.getValueAt(selectedRow, 2);
        String currentDate = (String) model.getValueAt(selectedRow, 3);

        JTextField stockInIdField = new JTextField(currentStockInId);
        JTextField productIdField = new JTextField(currentProductId);
        JTextField quantityField = new JTextField(currentQuantity);
        JTextField dateField = new JTextField(currentDate);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Stock In ID:"));
        panel.add(stockInIdField);
        panel.add(new JLabel("Product ID:"));
        panel.add(productIdField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Date:"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Stock In",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            model.setValueAt(stockInIdField.getText(), selectedRow, 0);
            model.setValueAt(productIdField.getText(), selectedRow, 1);
            model.setValueAt(quantityField.getText(), selectedRow, 2);
            model.setValueAt(dateField.getText(), selectedRow, 3);

            originalModel.setValueAt(stockInIdField.getText(), selectedRow, 0);
            originalModel.setValueAt(productIdField.getText(), selectedRow, 1);
            originalModel.setValueAt(quantityField.getText(), selectedRow, 2);
            originalModel.setValueAt(dateField.getText(), selectedRow, 3);
        }
    }

    private void deleteStockIn() {
        int selectedRow = stockInTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this stock in?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(selectedRow);
            originalModel.removeRow(selectedRow);
        }
    }

    public void refreshTable() {
    model.setRowCount(0);
    for (model.StockIn s : new StockInDAO().getAllStockIn()) {
        model.addRow(new Object[]{
                s.getStockInID(),
                s.getProductID(),
                s.getQuantity(),
                s.getDate(),
                s.getSupplierName(),
                s.getNotes()
        });
    }
}

    private void searchStockIn() {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) return;

        DefaultTableModel searchModel = new DefaultTableModel(
                new String[]{"Stock In ID", "Product ID", "Quantity", "Date"}, 0);

        for (int i = 0; i < originalModel.getRowCount(); i++) {
            String productId = originalModel.getValueAt(i, 1).toString().toLowerCase();
            if (productId.contains(keyword)) {
                Vector<?> row = (Vector<?>) originalModel.getDataVector().get(i);
                searchModel.addRow(row);
            }
        }

        stockInTable.setModel(searchModel);
        model = searchModel;
    }

    private void clearSearch() {
        stockInTable.setModel(originalModel);
        model = originalModel;
        searchField.setText("");
    }

    // Getters if needed
    public JTable getStockInTable() { return stockInTable; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getRefreshButton() { return refreshButton; }
    public JButton getBackButton() { return backButton; }
}