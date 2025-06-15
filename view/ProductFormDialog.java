package view;

import model.Product;
import model.Category;
import controller.CategoryController;
import controller.SubCategoryController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProductFormDialog extends JDialog {
    private boolean confirmed = false;

    // Input Fields
    private JTextField nameField, priceField, stockField;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> subCategoryComboBox;

    public ProductFormDialog(JFrame parent, String title, boolean modal, Product product) {
        super(parent, title, modal);
        setSize(400, 350);
        if (parent != null) {
            setLocationRelativeTo(parent); // Dialog muncul di tengah frame utama
        } else {
            setLocationRelativeTo(null); // Muncul di tengah layar
        }

        setLayout(new GridLayout(7, 2, 10, 10));

        // Inisialisasi UI
        JLabel nameLabel = new JLabel("Nama Produk:");
        nameField = new JTextField();

        JLabel categoryLabel = new JLabel("Pilih Kategori:");
        categoryComboBox = new JComboBox<>();
        loadCategories(); // Load kategori dari database/controller

        JLabel subCategoryLabel = new JLabel("Pilih Sub-Kategori:");
        subCategoryComboBox = new JComboBox<>();
        loadSubCategories(); // Load sub-kategori dari database/controller

        JLabel priceLabel = new JLabel("Harga:");
        priceField = new JTextField();

        JLabel stockLabel = new JLabel("Stok:");
        stockField = new JTextField();

        JButton saveButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");

        // Populate jika edit
        if (product != null) {
            nameField.setText(product.getProductName());
            priceField.setText(String.valueOf(product.getProductPrice()));
            stockField.setText(String.valueOf(product.getProductStock()));

            // Set combo box sesuai data produk
            categoryComboBox.setSelectedItem(product.getCategoryName());
            subCategoryComboBox.setSelectedItem(product.getSubCategoryName());
        }

        // Tambahkan ke layout
        add(nameLabel); add(nameField);
        add(categoryLabel); add(categoryComboBox);
        add(subCategoryLabel); add(subCategoryComboBox);
        add(priceLabel); add(priceField);
        add(stockLabel); add(stockField);
        add(new JLabel()); add(saveButton);
        add(new JLabel()); add(cancelButton);

        // Action Save
        saveButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Getter untuk hasil input
    public boolean isConfirmed() {
        return confirmed;
    }

    public String getProductName() {
        return nameField.getText().trim();
    }

    public String getCategoryName() {
        return (String) categoryComboBox.getSelectedItem();
    }

    public String getSubCategoryName() {
        return (String) subCategoryComboBox.getSelectedItem();
    }

    public float getProductPrice() {
        try {
            return Float.parseFloat(priceField.getText().trim());
        } catch (NumberFormatException ex) {
            return 0f;
        }
    }

    public int getProductStock() {
        try {
            return Integer.parseInt(stockField.getText().trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private void loadCategories() {
    CategoryController cc = new CategoryController();
    for (Category category : cc.getAllCategories()) { // Bisa juga pakai cc.getAllCategories() jika array
        categoryComboBox.addItem(category.getCategoryName());
    }
}  

    // Di dalam method loadSubCategories()
    private void loadSubCategories() {
        SubCategoryController sc = new SubCategoryController();
        for (String subCat : sc.getAllSubCategoryNames()) {
            subCategoryComboBox.addItem(subCat);
        }
    }
}