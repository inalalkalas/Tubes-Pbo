package view;

import model.User;
import dao.UserDAO;
import controller.UserController;
import view.UserFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StaffView extends JFrame {
    private JTable staffTable;
    private JButton addButton, editButton, deleteButton, refreshButton, backButton;
    private JButton searchButton, clearSearchButton;
    private JTextField searchField;
    private DefaultTableModel model;
    private String role;
    private UserController userController = new UserController();
    private DefaultTableModel originalModel;

    public StaffView(String role) {
        this.role = role;

        if (!role.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Akses ditolak. Anda bukan admin.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Staff Management - Role: " + role);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔹 Top Panel: Back & Search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(10, 36, 106));
        topPanel.setPreferredSize(new Dimension(getWidth(), 60));

        // Tombol Back
        backButton = new JButton("← Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(17, 50, 120));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose();
            new DashboardAdminView(role, "Admin");
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

        topPanel.add(backPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 🔹 Table
        String[] columnNames = {"Staff ID", "Name", "Role", "Email"};
        model = new DefaultTableModel(columnNames, 0);
        staffTable = new JTable(model);
        staffTable.setFillsViewportHeight(true);
        staffTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        staffTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        staffTable.getTableHeader().setBackground(new Color(10, 36, 106));
        staffTable.getTableHeader().setForeground(Color.WHITE);
        staffTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // 🔹 Tombol Bawah
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addButton = createStyledButton("➕ Add Staff");
        editButton = createStyledButton("✏️ Edit Staff");
        deleteButton = createStyledButton("🗑️ Delete Staff");
        refreshButton = createStyledButton("🔄 Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 🔹 Aksi tombol
        addButton.addActionListener(e -> userController.addStaff(this, model));
        editButton.addActionListener(e -> userController.editStaff(this, staffTable, model));
        deleteButton.addActionListener(e -> userController.deleteStaff(this, staffTable, model));
        refreshButton.addActionListener(e -> userController.refreshTable(model));
        searchButton.addActionListener(e -> searchStaff());
        clearSearchButton.addActionListener(e -> clearSearch());

        // Muat data awal
        userController.loadUsersToTable(model);

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

    private void searchStaff() {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) return;

        DefaultTableModel searchModel = new DefaultTableModel(
                new String[]{"Staff ID", "Name", "Role", "Email"}, 0);

        for (int i = 0; i < originalModel.getRowCount(); i++) {
            String staffName = originalModel.getValueAt(i, 1).toString().toLowerCase();
            if (staffName.contains(keyword)) {
                searchModel.addRow(originalModel.getDataVector().get(i));
            }
        }

        staffTable.setModel(searchModel);
        model = searchModel;
    }

    private void clearSearch() {
        staffTable.setModel(originalModel);
        model = originalModel;
        searchField.setText("");
    }
}