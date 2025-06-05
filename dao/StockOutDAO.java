package dao;

import model.StockOut;
import utils.DbConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class StockOutDAO {

    // Ambil semua stock_out dari database
    public List<StockOut> getAllStockOut() {
        List<StockOut> stockOuts = new ArrayList<>();
        String sql = "SELECT * FROM stock_out";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                stockOuts.add(new StockOut(
                        rs.getInt("stock_outID"),
                        rs.getInt("productID"),
                        rs.getString("product_name"),
                        rs.getInt("stock_out_quantity"),
                        rs.getTimestamp("stock_out_date").toLocalDateTime(),
                        rs.getInt("userID"),
                        rs.getString("destination"),
                        rs.getString("stock_out_notes")
                ));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data stock out: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return stockOuts;
    }

    // Tambah stock_out
    public boolean addStockOut(int productID, int quantity, int userID, String destination, String notes) {
        String sql = "INSERT INTO stock_out (productID, stock_out_quantity, userID, product_name, destination, stock_out_notes) VALUES (?, ?, ?, (SELECT product_name FROM product WHERE productID = ?), ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productID);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, userID);
            pstmt.setInt(4, productID);
            pstmt.setString(5, destination);
            pstmt.setString(6, notes);

            boolean success = pstmt.executeUpdate() > 0;

            if (success) {
                updateProductStock(productID, quantity, false); // Kurangi stok
            }

            return success;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambahkan stock out: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Update stock_out
    public boolean updateStockOut(StockOut stockOut) {
        int oldQuantity = getOldQuantity(stockOut.getStockOutID());
        if (oldQuantity == -1) return false;

        String sql = "UPDATE stock_out SET productID = ?, stock_out_quantity = ?, destination = ?, stock_out_notes = ? WHERE stock_outID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockOut.getProductID());
            pstmt.setInt(2, stockOut.getQuantity());
            pstmt.setString(3, stockOut.getDestination());
            pstmt.setString(4, stockOut.getNotes());
            pstmt.setInt(5, stockOut.getStockOutID());

            boolean success = pstmt.executeUpdate() > 0;

            if (success) {
                int diff = stockOut.getQuantity() - oldQuantity;
                updateProductStock(stockOut.getProductID(), diff, false);
            }

            return success;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui stock out: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Hapus stock_out
    public boolean deleteStockOut(int stockOutID) {
        int productID = getProductIDFromStockOut(stockOutID);
        int quantity = getStockOutQuantity(stockOutID);

        String sql = "DELETE FROM stock_out WHERE stock_outID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockOutID);
            boolean success = pstmt.executeUpdate() > 0;

            if (success && productID != -1 && quantity != -1) {
                updateProductStock(productID, quantity, true); // Tambah kembali stok
            }

            return success;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus stock out: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Update stok produk
    private void updateProductStock(int productID, int quantity, boolean isStockIn) {
        String sql = "UPDATE product SET product_stock = product_stock " +
                     (isStockIn ? "+ " : "- ") + "? WHERE productID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui stok: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Ambil jumlah lama sebelum update
    private int getOldQuantity(int stockOutID) {
        String sql = "SELECT stock_out_quantity FROM stock_out WHERE stock_outID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockOutID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock_out_quantity");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Ambil productID dari stock_out
    private int getProductIDFromStockOut(int stockOutID) {
        String sql = "SELECT productID FROM stock_out WHERE stock_outID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockOutID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("productID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Ambil jumlah stock_out
    private int getStockOutQuantity(int stockOutID) {
        String sql = "SELECT stock_out_quantity FROM stock_out WHERE stock_outID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockOutID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock_out_quantity");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}