package dao;

import model.SubCategory;
import utils.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class SubCategoryDAO {

    // Ambil semua sub-kategori dari database
    public List<SubCategory> getAllSubCategories() {
        List<SubCategory> subCategories = new ArrayList<>();
        String sql = "SELECT sub_categoryID, sub_category_name FROM sub_category";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                subCategories.add(new SubCategory(
                        rs.getInt("sub_categoryID"),
                        rs.getString("sub_category_name")
                ));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data sub-kategori: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return subCategories;
    }

    // Cari sub-kategori berdasarkan nama
    public SubCategory getSubCategoryByName(String name) {
        String sql = "SELECT sub_categoryID, sub_category_name FROM sub_category WHERE sub_category_name = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new SubCategory(rs.getInt("sub_categoryID"), rs.getString("sub_category_name"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencari sub-kategori: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // Tambah sub-kategori baru jika belum ada
    public boolean addSubCategoryIfNotExists(String subCategoryName) {
        if (getSubCategoryByName(subCategoryName) == null) {
            int lastId = getLastSubCategoryId();
            int newId = lastId + 1;

            String sql = "INSERT INTO sub_category (sub_categoryID, sub_category_name) VALUES (?, ?)";
            try (Connection conn = DbConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, newId);
                pstmt.setString(2, subCategoryName);

                return pstmt.executeUpdate() > 0;

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal menambahkan sub-kategori: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true; // Sudah ada → tidak perlu tambah
    }

    // Ambil ID terakhir dan tambah 1
    public int getLastSubCategoryId() {
        String sql = "SELECT MAX(sub_categoryID) FROM sub_category";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int lastId = rs.getInt(1);
                return lastId > 0 ? lastId : 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal ambil ID terakhir sub-kategori: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }
}