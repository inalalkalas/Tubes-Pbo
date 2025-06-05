package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;

public class DashboardAdminView extends JFrame {

    private JPanel homePanel;
    private JPanel productPanel;
    private JPanel stockInPanel;
    private JPanel stockOutPanel;
    private JPanel staffPanel;
    private JPanel logoutPanel;

    public DashboardAdminView(String role, String username) {
        setTitle("Admin Dashboard - Role: " + role);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- HEADER PANEL ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout()); // Menggunakan BorderLayout untuk memudahkan penempatan elemen
        headerPanel.setBackground(new Color(10, 36, 106)); // Dark blue
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        // WMS Label (di pojok kiri)
        JLabel wmsLabel = new JLabel("WMS");
        wmsLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        wmsLabel.setForeground(Color.WHITE);
        wmsLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // Padding kiri

        // Admin | Username Label (di pojok kanan)
        JLabel adminLabel = new JLabel("Admin | " + username);
        adminLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        adminLabel.setForeground(Color.WHITE);
        adminLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); // Padding kanan

        // Menambahkan ke headerPanel
        headerPanel.add(wmsLabel, BorderLayout.WEST); // WMS di sebelah kiri
        headerPanel.add(adminLabel, BorderLayout.EAST); // Admin | Username di sebelah kanan

        add(headerPanel, BorderLayout.NORTH);

        // --- SIDEBAR PANEL ---
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(10, 36, 106)); // Dark blue
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));

        // Home Panel
        homePanel = createSidebarPanel("🏠 Home");
        homePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(DashboardAdminView.this, "Home clicked");
            }
        });
        sidebarPanel.add(homePanel);

        // Product Panel
        productPanel = createSidebarPanel("📦 Product");
        productPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ProductView(role); // Tampilkan ProductView untuk admin
                dispose(); // Menutup DashboardAdminView
            }
        });
        sidebarPanel.add(productPanel);

        // Stock In Panel
        stockInPanel = createSidebarPanel("📥 Stock In");
        stockInPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new StockInView(role); // Tampilkan StockInView
                dispose(); // Menutup DashboardAdminView
            }
        });
        sidebarPanel.add(stockInPanel);

        // Stock Out Panel
        stockOutPanel = createSidebarPanel("📤 Stock Out");
        stockOutPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new StockOutView(role); // Tampilkan StockOutView
                dispose(); // Menutup DashboardAdminView
            }
        });
        sidebarPanel.add(stockOutPanel);

        // Staff Panel
        staffPanel = createSidebarPanel("👤 Staff");
        staffPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new StaffView(role); // Tampilkan StaffView untuk admin
                dispose(); // Menutup DashboardAdminView
            }
        });
        sidebarPanel.add(staffPanel);

        // Logout Panel
        logoutPanel = createSidebarPanel("🚪 Log Out");
        logoutPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(DashboardAdminView.this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose(); // Menutup dashboard
                    new LoginView(); // Kembali ke login
                }
            }
        });
        sidebarPanel.add(logoutPanel);

        add(sidebarPanel, BorderLayout.WEST);

        // --- CONTENT PANEL ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Welcome Message based on time of day
        LocalTime now = LocalTime.now();
        String greeting;

        if (now.isAfter(LocalTime.of(5, 0)) && now.isBefore(LocalTime.of(12, 0))) {
            greeting = "Good Morning";
        } else if (now.isAfter(LocalTime.of(12, 0)) && now.isBefore(LocalTime.of(18, 0))) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Night";
        }

        JLabel welcomeMessage = new JLabel(
            String.format("%s, %s", greeting, username),
            SwingConstants.CENTER
        );
        welcomeMessage.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeMessage.setForeground(new Color(44, 62, 80)); // Soft dark color
        welcomeMessage.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        contentPanel.add(welcomeMessage, BorderLayout.NORTH);

        // --- Statistik Section ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 20)); // 4 kolom dengan padding
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Statistik Produk
        JPanel productStats = createStatBox("📦 Products", "120", "Total Products");
        statsPanel.add(productStats);

        // Statistik Stok Masuk
        JPanel stockInStats = createStatBox("📥 Stock In", "50", "Items Received");
        statsPanel.add(stockInStats);

        // Statistik Stok Keluar
        JPanel stockOutStats = createStatBox("📤 Stock Out", "30", "Items Dispatched");
        statsPanel.add(stockOutStats);

        // Statistik Staf
        JPanel staffStats = createStatBox("👤 Staff", "10", "Active Staff");
        statsPanel.add(staffStats);

        contentPanel.add(statsPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // --- Helper Methods ---

    private JPanel createSidebarPanel(String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(10, 36, 106)); // Match sidebar background
        panel.setPreferredSize(new Dimension(200, 40));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);

        panel.add(label);

        // Hover effect
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(17, 50, 120)); // Lighter shade
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(10, 36, 106)); // Original color
            }
        });

        return panel;
    }

    private JPanel createStatBox(String icon, String value, String description) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setForeground(new Color(10, 36, 106));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(iconLabel, BorderLayout.NORTH);

        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(new Color(44, 62, 80));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(valueLabel, BorderLayout.CENTER);

        // Description
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(130, 130, 130));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(descLabel, BorderLayout.SOUTH);

        return panel;
    }
}