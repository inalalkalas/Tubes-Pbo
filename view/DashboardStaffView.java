package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class DashboardStaffView extends JFrame {

    private JButton btnStockIn, btnStockOut, btnLogout;
    private JPanel stockInPanel, stockOutPanel, logoutPanel;

    public DashboardStaffView(String role) {
        setTitle("Staff Dashboard - Role: " + role);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- HEADER PANEL ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(10, 36, 106)); // Dark blue
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel wmsLabel = new JLabel("WMS");
        wmsLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        wmsLabel.setForeground(Color.WHITE);
        wmsLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JLabel staffLabel = new JLabel("Staff | User");
        staffLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        staffLabel.setForeground(Color.WHITE);
        staffLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        headerPanel.add(wmsLabel, BorderLayout.WEST);
        headerPanel.add(staffLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- SIDEBAR PANEL ---
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(10, 36, 106));
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));

        // Di dalam constructor DashboardStaffView.java
        JPanel productPanel = createSidebarPanel("📦 Product");
        productPanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            new ProductView("staff"); // Staff hanya bisa lihat dan tambah
            dispose(); // Tutup dashboard staff
            }
        });
        sidebarPanel.add(productPanel);


        // Stock In Panel
        stockInPanel = createSidebarPanel("📥 Stock In");
        stockInPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new StockInView(role); // Staff bisa lihat stock in
                dispose(); // Tutup dashboard
            }
        });
        sidebarPanel.add(stockInPanel);

        // Stock Out Panel
        stockOutPanel = createSidebarPanel("📤 Stock Out");
        stockOutPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new StockOutView(role); // Staff bisa tambah stock out
                dispose(); // Tutup dashboard staff
            }
        });
        sidebarPanel.add(stockOutPanel);

        // Logout Panel
        logoutPanel = createSidebarPanel("🚪 Log Out");
        logoutPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(DashboardStaffView.this,
                        "Apakah Anda yakin ingin keluar?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose(); // Tutup dashboard
                    new LoginView(); // Kembali ke halaman login
                }
            }
        });
        sidebarPanel.add(logoutPanel);

        add(sidebarPanel, BorderLayout.WEST);

        // --- CONTENT PANEL ---
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

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
                String.format("%s, %s", greeting, "Staff"),
                SwingConstants.CENTER
        );
        welcomeMessage.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeMessage.setForeground(new Color(44, 62, 80)); // Soft dark color
        welcomeMessage.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        contentPanel.add(welcomeMessage, BorderLayout.NORTH);

        // --- Statistik Section ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Statistik Produk
        statsPanel.add(createStatBox("📦 Products", "120", "Total Items"));

        // Statistik Stok Masuk
        statsPanel.add(createStatBox("📥 Stock In", "50", "Items Received"));

        // Statistik Stok Keluar
        statsPanel.add(createStatBox("📤 Stock Out", "30", "Items Dispatched"));

        contentPanel.add(statsPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Membuat panel sidebar dengan hover effect
    private JPanel createSidebarPanel(String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(10, 36, 106));
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

    // Membuat box statistik
    private JPanel createStatBox(String icon, String value, String description) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setForeground(new Color(10, 36, 106));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(iconLabel, BorderLayout.NORTH);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(new Color(44, 62, 80));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(valueLabel, BorderLayout.CENTER);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(130, 130, 130));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(descLabel, BorderLayout.SOUTH);

        return panel;
    }
}