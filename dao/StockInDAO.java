package dao;

import model.StockIn;
import utils.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class StockInDAO {

    // Ambil semua stock_in dari database
    public List<StockIn> getAllStockIn() {
        List<StockIn> stockIns = new ArrayList<>();
        String sql = "SELECT * FROM stock_in";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                stockIns.add(new StockIn(
                        rs.getInt("stock_inID"),
                        rs.getInt("productID"),
                        rs.getString("product_name"),
                        rs.getInt("stock_in_quantity"),
                        rs.getTimestamp("stock_in_date").toLocalDateTime(),
                        rs.getInt("userID"),
                        rs.getString("suplier_name"),
                        rs.getString("notes")
                ));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data stock in: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return stockIns;
    }

    // Tambah stock_in baru
    public boolean addStockIn(int productID, int quantity, int userID, String supplierName, String notes) {
        String sql = "INSERT INTO stock_in (productID, stock_in_quantity, userID, product_name, suplier_name, notes) VALUES (?, ?, ?, (SELECT product_name FROM product WHERE productID = ?), ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productID);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, userID);
            pstmt.setInt(4, productID); // Untuk ambil product_name
            pstmt.setString(5, supplierName);
            pstmt.setString(6, notes);

            boolean success = pstmt.executeUpdate() > 0;

            if (success) {
                updateProductStock(productID, quantity, true); // Tambah stok
            }

            return success;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambahkan stock in: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Update stock_in
    public boolean updateStockIn(StockIn stockIn) {
        String sql = "UPDATE stock_in SET productID = ?, stock_in_quantity = ?, suplier_name = ?, notes = ? WHERE stock_inID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockIn.getProductID());
            pstmt.setInt(2, stockIn.getQuantity());
            pstmt.setString(3, stockIn.getSupplierName());
            pstmt.setString(4, stockIn.getNotes());
            pstmt.setInt(5, stockIn.getStockInID());

            boolean success = pstmt.executeUpdate() > 0;

            if (success) {
                updateProductStock(stockIn.getProductID(), stockIn.getQuantity(), true);
            }

            return success;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui stock in: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Hapus stock_in
    public boolean deleteStockIn(int stockInID) {
        int productID = getProductIDFromStockIn(stockInID);
        int quantity = getStockInQuantity(stockInID);

        String sql = "DELETE FROM stock_in WHERE stock_inID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockInID);

            boolean success = pstmt.executeUpdate() > 0;

            if (success && productID != -1 && quantity != -1) {
                updateProductStock(productID, quantity, false); // Kurangi stok karena dihapus
            }

            return success;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus stock in: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Ambil ID produk dari stock_in
    private int getProductIDFromStockIn(int stockInID) {
        String sql = "SELECT productID FROM stock_in WHERE stock_inID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockInID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("productID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Ambil jumlah dari stock_in
    private int getStockInQuantity(int stockInID) {
        String sql = "SELECT stock_in_quantity FROM stock_in WHERE stock_inID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockInID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock_in_quantity");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Update stok produk saat tambah/kurangi stock_in
    private void updateProductStock(int productID, int quantity, boolean isAdd) {
        String sql = "UPDATE product SET product_stock = product_stock " +
                     (isAdd ? "+" : "-") + " ? WHERE productID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui stok: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}