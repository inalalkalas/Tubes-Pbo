package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12784653?useSSL=false&serverTimezone=UTC";
    private static final String USER = "sql12784653";
    private static final String PASSWORD = "ApWDc7ZUXe";
    
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn != null) {
                System.out.println("Database connection established successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        System.out.println("Cek koneksi: " + (conn != null ? "✅ Berhasil terhubung ke database!" : "❌ Gagal terhubung ke database."));
        
        // Always close the connection when done
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}