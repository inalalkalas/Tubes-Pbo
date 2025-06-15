package dao;

import model.User;
import utils.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class UserDAO {

    // Ambil ID user terakhir dari database
    public int getLastUserID() {
        String sql = "SELECT MAX(userID) FROM user";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int lastId = rs.getInt(1);
                return lastId > 0 ? lastId : 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal ambil ID terakhir user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    // Tambah user baru dengan auto ID
    public boolean addUser(User user) {
        int lastId = getLastUserID();
        int newId = lastId + 1;

        String sql = "INSERT INTO user (userID, user_name, user_password, user_role, user_email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newId);
            pstmt.setString(2, user.getUserName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getEmail());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambahkan user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Ambil semua user dari database
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT userID, user_name, user_password, user_role, user_email FROM user";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("userID"),
                        rs.getString("user_name"),
                        rs.getString("user_password"),
                        rs.getString("user_role"),
                        null, // lastLogin → bisa diisi nanti via DAO
                        null, // status → bisa diisi nanti
                        rs.getString("user_email")
                ));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil daftar user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return users;
    }

    // Cari user berdasarkan ID
    public User getUserById(int userId) {
        String sql = "SELECT userID, user_name, user_password, user_role, user_email FROM user WHERE userID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("userID"),
                        rs.getString("user_name"),
                        rs.getString("user_password"),
                        rs.getString("user_role"),
                        null,
                        null,
                        rs.getString("user_email")
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mencari user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // Validasi apakah username unik
    public boolean isUserNameUnique(String userName, int excludeUserId) {
        String sql = "SELECT COUNT(*) FROM user WHERE user_name = ? AND userID != ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userName);
            pstmt.setInt(2, excludeUserId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Jika count = 0 → nama unik
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memvalidasi username: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Validasi apakah email unik
    public boolean isEmailUnique(String email, int excludeUserId) {
        String sql = "SELECT COUNT(*) FROM user WHERE user_email = ? AND userID != ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setInt(2, excludeUserId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memvalidasi email: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE user SET user_name = ?, user_password = ?, user_role = ?, user_email = ? WHERE userID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getEmail());
            pstmt.setInt(5, user.getUserID());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM user WHERE userID = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}