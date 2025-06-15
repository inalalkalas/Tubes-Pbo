package controller;

import view.LoginView;
import view.DashboardAdminView;
import view.DashboardStaffView;
import utils.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JOptionPane;

public class LoginController {
    private LoginView loginView;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCKOUT_DURATION_MINUTES = 10;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;

        // Tambahkan action listener untuk tombol login
        loginView.getLoginButton().addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        // Validasi input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Harap isi username dan password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Autentikasi ke database
        try (Connection connection = DbConnection.getConnection()) {
            String sql = "SELECT * FROM user WHERE user_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String storedPassword = rs.getString("user_password");
                    String role = rs.getString("user_role");
                    int failedAttempt = rs.getInt("failed_attempt");
                    java.sql.Timestamp lockoutTime = rs.getTimestamp("lockout_time");

                    // Cek apakah user sedang lockout
                    if (lockoutTime != null) {
                        LocalDateTime lockoutEnd = lockoutTime.toLocalDateTime().plusMinutes(LOCKOUT_DURATION_MINUTES);
                        if (LocalDateTime.now().isBefore(lockoutEnd)) {
                            long minutesLeft = java.time.Duration.between(LocalDateTime.now(), lockoutEnd).toMinutes();
                            JOptionPane.showMessageDialog(loginView, "Akun dikunci. Coba lagi dalam " + minutesLeft + " menit.", "Locked", JOptionPane.ERROR_MESSAGE);
                            return;
                        } else {
                            // Jika waktu lockout sudah lewat, reset lockout
                            resetFailedAttempts(username);
                        }
                    }

                    // Cek password
                    if (storedPassword.equals(password)) {
                        JOptionPane.showMessageDialog(loginView, "Login berhasil sebagai " + role, "Success", JOptionPane.INFORMATION_MESSAGE);

                        // Update last login dan reset failed_attempt
                        updateLastLoginAndResetAttempts(username);

                        // Buka dashboard berdasarkan role
                        if (role.equalsIgnoreCase("admin")) {
                            new DashboardAdminView(role, role).setVisible(true);
                        } else if (role.equalsIgnoreCase("staff")) {
                            new DashboardStaffView(role).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(loginView, "Role tidak dikenali!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Tutup jendela login
                        loginView.dispose();
                    } else {
                        // Password salah
                        failedAttempt++;
                        if (failedAttempt >= MAX_FAILED_ATTEMPTS) {
                            setLockout(username);
                            JOptionPane.showMessageDialog(loginView, "Terlalu banyak percobaan gagal. Akun dikunci selama " + LOCKOUT_DURATION_MINUTES + " menit.", "Locked", JOptionPane.ERROR_MESSAGE);
                        } else {
                            updateFailedAttempts(username, failedAttempt);
                            JOptionPane.showMessageDialog(loginView, "Password salah. Percobaan ke-" + failedAttempt + " dari " + MAX_FAILED_ATTEMPTS, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    // Username tidak ditemukan
                    JOptionPane.showMessageDialog(loginView, "Username tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(loginView, "Gagal koneksi ke database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFailedAttempts(String username) {
        try (Connection connection = DbConnection.getConnection()) {
            String sql = "UPDATE user SET failed_attempt = 0, lockout_time = NULL WHERE user_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateFailedAttempts(String username, int failedAttempt) {
        try (Connection connection = DbConnection.getConnection()) {
            String sql = "UPDATE user SET failed_attempt = ? WHERE user_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, failedAttempt);
                stmt.setString(2, username);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setLockout(String username) {
        try (Connection connection = DbConnection.getConnection()) {
            String sql = "UPDATE user SET lockout_time = NOW(), failed_attempt = 0 WHERE user_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateLastLoginAndResetAttempts(String username) {
        try (Connection connection = DbConnection.getConnection()) {
            String sql = "UPDATE user SET user_last_login = NOW(), failed_attempt = 0, lockout_time = NULL WHERE user_name = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
