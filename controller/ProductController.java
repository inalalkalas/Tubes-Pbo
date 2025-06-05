package controller;

import dao.CategoryDAO;
import dao.ProductDAO;
import dao.SubCategoryDAO;
import model.Product;
import view.ProductFormDialog;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Category;
import model.SubCategory;

public class ProductController {

    private ProductDAO productDao = new ProductDAO();

    // Load semua produk ke dalam tabel
    public void loadProductsToTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Product product : productDao.getAllProducts()) {
            Object[] row = {
                    product.getProductID(),
                    product.getProductName(),
                    product.getCategoryID(),
                    product.getProductPrice(),
                    product.getProductStock()
            };
            model.addRow(row);
        }
    }

    // Tambah produk baru
    public void addProduct(JFrame parent, DefaultTableModel model) {
    ProductFormDialog dialog = new ProductFormDialog(parent, "Tambah Produk Baru", true, null);
    if (dialog.isConfirmed()) {
        String productName = dialog.getProductName();
        String categoryName = dialog.getCategoryName();
        String subCategoryName = dialog.getSubCategoryName();
        float productPrice = dialog.getProductPrice();
        int productStock = dialog.getProductStock();
        int productMax = 99;

        if (productStock > productMax) {
            JOptionPane.showMessageDialog(parent, 
                "Stok tidak boleh melebihi " + productMax, 
                "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi input
        if (productName.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nama produk harus diisi.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nama kategori harus diisi.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (subCategoryName.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Nama sub-kategori harus diisi.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pastikan sub-kategori tersedia
        int subCategoryId = new SubCategoryController().ensureAndGetSubCategoryId(subCategoryName);
        if (subCategoryId == -1) {
            return;
        }

        // Pastikan kategori tersedia
        int categoryId = new CategoryController().ensureAndGetCategoryId(categoryName, subCategoryName);
        if (categoryId == -1) {
            return;
        }

        // Simpan produk
        Product newProduct = new Product(0, productName, categoryId, productPrice, productStock);
        if (productDao.addProduct(newProduct)) {
            JOptionPane.showMessageDialog(parent, "Produk berhasil ditambahkan.");
            refreshTable(model);
        } else {
            JOptionPane.showMessageDialog(parent, "Gagal menambahkan produk.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    // Edit produk berdasarkan baris yang dipilih
    public void editProduct(JFrame parent, JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parent, "Pilih produk untuk diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) table.getValueAt(selectedRow, 0);
        Product product = productDao.getProductById(productId);

        if (product == null) {
            JOptionPane.showMessageDialog(parent, "Produk tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProductFormDialog dialog = new ProductFormDialog(parent, "Edit Produk", true, product);
        if (dialog.isConfirmed()) {
            product.setProductName(dialog.getProductName());
            product.setCategoryName(dialog.getCategoryName());
            product.setProductPrice(dialog.getProductPrice());
            product.setProductStock(dialog.getProductStock());

            if (productDao.updateProduct(product)) {
                JOptionPane.showMessageDialog(parent, "Produk berhasil diperbarui.");
                refreshTable(model);
            } else {
                JOptionPane.showMessageDialog(parent, "Gagal memperbarui produk.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Hapus produk berdasarkan baris yang dipilih
    public void deleteProduct(JFrame parent, JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(parent, "Pilih produk untuk dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(parent, "Yakin ingin menghapus produk ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (productDao.deleteProduct(productId)) {
                JOptionPane.showMessageDialog(parent, "Produk berhasil dihapus.");
                refreshTable(model);
            } else {
                JOptionPane.showMessageDialog(parent, "Gagal menghapus produk.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Refresh tabel
    public void refreshTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (Product product : productDao.getAllProducts()) {
            Object[] row = {
                    product.getProductID(),
                    product.getProductName(),
                    product.getCategoryID(),
                    product.getProductPrice(),
                    product.getProductStock()
            };
            model.addRow(row);
        }
    }

}