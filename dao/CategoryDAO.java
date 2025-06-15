package dao;

import model.Category;
import utils.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class CategoryDAO {

    // Ambil semua kategori dari database
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT categoryID, category_name FROM category";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int categoryId = rs.getInt("categoryID");
                String categoryName = rs.getString("category_name");

                categories.add(new Category(categoryId, categoryName));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil daftar kategori: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return categories;
    }

    // Cari kategori berdasarkan nama
    public Category getCategoryByName(String categoryName) {
        String sql = "SELECT categoryID, category_name FROM category WHERE category_name = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categoryName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Category(rs.getInt("categoryID"), rs.getString("category_name"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencari kategori: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // Ambil ID terakhir dan tambah 1
    public int getLastCategoryId() {
        String sql = "SELECT MAX(categoryID) FROM category";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int lastId = rs.getInt(1);
                return lastId > 0 ? lastId : 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal ambil ID terakhir kategori: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    // Tambah kategori baru
    public boolean addCategory(String categoryName, int subCategoryId) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nama kategori kosong.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        int lastId = getLastCategoryId();
        int newId = lastId + 1;

        String sql = "INSERT INTO category (categoryID, category_name, sub_categoryID) VALUES (?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newId);
            pstmt.setString(2, categoryName);
            pstmt.setInt(3, subCategoryId); // Harus valid!

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambahkan kategori: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}