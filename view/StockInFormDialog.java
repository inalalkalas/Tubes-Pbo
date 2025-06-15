package view;

import model.Product;
import dao.ProductDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;

public class StockInFormDialog extends JDialog {
    private boolean confirmed = false;

    private JComboBox<String> productComboBox;
    private JTextField quantityField, supplierField, notesField;

    public StockInFormDialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
        setSize(500, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Panel Form (Grid bagian atas)
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        // Produk
        JLabel productLabel = new JLabel("Pilih Produk:");
        productLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productComboBox = new JComboBox<>();
        loadProducts();
        productComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Jumlah
        JLabel quantityLabel = new JLabel("Jumlah Masuk:");
        quantityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        quantityField = new JTextField();
        quantityField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Supplier
        JLabel supplierLabel = new JLabel("Supplier:");
        supplierLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        supplierField = new JTextField();
        supplierField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Tanggal
        JLabel dateLabel = new JLabel("Tanggal:");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField dateField = new JTextField(LocalDateTime.now().toString());
        dateField.setEditable(false);
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateField.setBackground(new Color(240, 240, 240)); // Abu-abu muda

        // Catatan
        JLabel notesLabel = new JLabel("Catatan:");
        notesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notesField = new JTextField();
        notesField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Tambahkan semua field ke panel form
        formPanel.add(productLabel); formPanel.add(productComboBox);
        formPanel.add(quantityLabel); formPanel.add(quantityField);
        formPanel.add(supplierLabel); formPanel.add(supplierField);
        formPanel.add(dateLabel); formPanel.add(dateField);
        formPanel.add(notesLabel); formPanel.add(notesField);

        // Tombol Simpan & Batal
        JButton saveButton = createStyledButton("Simpan");
        JButton cancelButton = createStyledButton("Batal");

        // Panel tombol - letakkan di bagian bawah, rata kanan
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Action Save
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void loadProducts() {
        ProductDAO productDao = new ProductDAO();
        for (Product p : productDao.getAllProducts()) {
            productComboBox.addItem(p.getProductName());
        }
    }

    private boolean validateInput() {
        try {
            Integer.parseInt(quantityField.getText());
            if (quantityField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Jumlah harus diisi.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (productComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih produk dari daftar.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(10, 36, 106));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 30));
        return btn;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getSelectedProductName() {
        return (String) productComboBox.getSelectedItem();
    }

    public int getQuantity() {
        return Integer.parseInt(quantityField.getText());
    }

    public String getSupplierName() {
        return supplierField.getText();
    }

    public String getNotes() {
        return notesField.getText();
    }
}