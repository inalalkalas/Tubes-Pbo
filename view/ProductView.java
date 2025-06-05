package view;

import model.Product;
import controller.ProductController;
import utils.DbConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ProductView extends JFrame {
    private JTable productTable;
    private JButton addButton, editButton, deleteButton, refreshButton, backButton;
    private JButton searchButton, clearSearchButton;
    private JTextField searchField;
    private DefaultTableModel model;
    private DefaultTableModel originalModel;
    private String role;
    private ProductController controller = new ProductController();

    public ProductView(String role) {
        this.role = role;

        setTitle("Product Management - Role: " + role);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- HEADER PANEL ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(10, 36, 106)); // Dark blue
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        // Back Button (kembali ke dashboard sesuai role)
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

        // Search Panel
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
        searchPanel.add(new JLabel("Cari Berdasarkan Nama:", SwingConstants.RIGHT));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearSearchButton);

        headerPanel.add(backPanel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- TABLE PANEL ---
        String[] columnNames = {"Product ID", "Name", "Category", "Price", "Stock"};
        model = new DefaultTableModel(columnNames, 0);
        originalModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(model);
        productTable.setFillsViewportHeight(true);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        productTable.getTableHeader().setBackground(new Color(10, 36, 106));
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // --- BUTTON PANEL ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addButton = createStyledButton("➕ Add Product");
        editButton = createStyledButton("✏️ Edit Product");
        deleteButton = createStyledButton("🗑️ Delete Product");
        refreshButton = createStyledButton("🔄 Refresh");

        // Hanya admin yang bisa hapus
        if ("staff".equals(role)) {
            deleteButton.setEnabled(false);
            deleteButton.setVisible(false);
        }

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- ACTION LISTENER ---
        addButton.addActionListener(e -> controller.addProduct(this, model));
        editButton.addActionListener(e -> controller.editProduct(this, productTable, model));
        deleteButton.addActionListener(e -> controller.deleteProduct(this, productTable, model));
        refreshButton.addActionListener(e -> controller.refreshTable(model));
        searchButton.addActionListener(e -> searchProduct());
        clearSearchButton.addActionListener(e -> clearSearch());

        // Muat data awal
        controller.loadProductsToTable(model);
        originalModel.setRowCount(0);
        for (int i = 0; i < model.getRowCount(); i++) {
            Object[] row = new Object[5];
            for (int j = 0; j < 5; j++) {
                row[j] = model.getValueAt(i, j);
            }
            originalModel.addRow(row);
        }

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(10, 36, 106));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void searchProduct() {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) return;

        model.setRowCount(0);
        for (int i = 0; i < originalModel.getRowCount(); i++) {
            String productName = originalModel.getValueAt(i, 1).toString().toLowerCase();
            if (productName.contains(keyword)) {
                model.addRow(originalModel.getDataVector().get(i));
            }
        }
    }

    private void clearSearch() {
        model.setRowCount(0);
        for (int i = 0; i < originalModel.getRowCount(); i++) {
            model.addRow(originalModel.getDataVector().get(i));
        }
        searchField.setText("");
    }
}