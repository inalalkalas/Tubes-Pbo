package dao;

import model.Product;
import utils.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ProductDAO {

    // Ambil semua produk dari database
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT productID, product_name, categoryID, product_price, product_stock FROM product";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("productID"),
                        rs.getString("product_name"),
                        rs.getInt("categoryID"),
                        rs.getFloat("product_price"),
                        rs.getInt("product_stock")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data produk: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return products;
    }

    // Tambah produk baru
    public boolean addProduct(Product product) {
    if (product.getProductStock() > product.getProductMax()) {
        JOptionPane.showMessageDialog(null, 
            "Gagal menambahkan produk: Stok tidak boleh melebihi " + product.getProductMax(), 
            "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    String sql = "INSERT INTO product (product_name, categoryID, product_price, product_stock, product_max) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, product.getProductName());
        pstmt.setInt(2, product.getCategoryID());
        pstmt.setFloat(3, product.getProductPrice());
        pstmt.setInt(4, product.getProductStock());
        pstmt.setInt(5, product.getProductMax());

        return pstmt.executeUpdate() > 0;

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal menambahkan produk: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}

    // Update produk
    public boolean updateProduct(Product product) {
        if (product == null || product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama produk kosong.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String sql = "UPDATE product SET product_name = ?, categoryID = ?, product_price = ?, product_stock = ? WHERE productID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getProductName());
            pstmt.setInt(2, product.getCategoryID());
            pstmt.setFloat(3, product.getProductPrice());
            pstmt.setInt(4, product.getProductStock());
            pstmt.setInt(5, product.getProductID());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui produk: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Hapus produk berdasarkan ID
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE productID = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus produk: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Ambil satu produk berdasarkan NAMA PRODUK
    public Product getProductByName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama produk kosong.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String sql = "SELECT productID, product_name, categoryID, product_price, product_stock FROM product WHERE product_name = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("productID"),
                        rs.getString("product_name"),
                        rs.getInt("categoryID"),
                        rs.getFloat("product_price"),
                        rs.getInt("product_stock")
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil detail produk: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null; // Jika tidak ditemukan
    }

    // Cari produk berdasarkan keyword (LIKE)
    public List<Product> searchProductsByName(String keyword) {
        List<Product> results = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Kata kunci pencarian kosong.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return results;
        }

        String sql = "SELECT productID, product_name, categoryID, product_price, product_stock FROM product WHERE product_name LIKE ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(new Product(
                        rs.getInt("productID"),
                        rs.getString("product_name"),
                        rs.getInt("categoryID"),
                        rs.getFloat("product_price"),
                        rs.getInt("product_stock")
                ));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencari produk: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return results;
    }

    public Product getProductById(int productId) {
        String sql = "SELECT productID, product_name, categoryID, product_price, product_stock FROM product WHERE productID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("productID"),
                        rs.getString("product_name"),
                        rs.getInt("categoryID"),
                        rs.getFloat("product_price"),
                        rs.getInt("product_stock")
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil produk: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null; // Jika tidak ditemukan
    }
}