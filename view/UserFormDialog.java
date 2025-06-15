package view;

import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class UserFormDialog extends JDialog {
    private boolean confirmed = false;

    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public UserFormDialog(JFrame parent, String title, boolean modal, User user) {
        super(parent, title, modal);
        setSize(500, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Panel form utama
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        // Label & Field
        JLabel nameLabel = new JLabel("Nama Staff:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel roleLabel = new JLabel("Role (admin/staff):");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleComboBox = new JComboBox<>(new String[]{"admin", "staff"});
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleComboBox.setBackground(Color.WHITE);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Populate jika mode edit
        if (user != null) {
            nameField.setText(user.getUserName());
            passwordField.setText(user.getPassword());
            roleComboBox.setSelectedItem(user.getRole());
            emailField.setText(user.getEmail());
        }

        // Tambahkan ke layout
        formPanel.add(nameLabel); formPanel.add(nameField);
        formPanel.add(passwordLabel); formPanel.add(passwordField);
        formPanel.add(roleLabel); formPanel.add(roleComboBox);
        formPanel.add(emailLabel); formPanel.add(emailField);

        // Panel tombol
        JButton saveButton = createStyledButton("Simpan");
        JButton cancelButton = createStyledButton("Batal");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Action Save
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Getter
    public boolean isConfirmed() {
        return confirmed;
    }

    public String getUserName() {
        return nameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }

    public String getRole() {
        return (String) roleComboBox.getSelectedItem();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    // Validasi input
    private boolean validateInput() {
        String name = getUserName();
        String password = getPassword();
        String email = getEmail();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama harus diisi.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password tidak boleh kosong.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email harus diisi.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Format email salah.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    // Validasi format email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(\\.[a-zA-Z0-9_+&*-]+)*@([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    // Styling tombol
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(10, 36, 106)); // Biru tua
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 30));
        return btn;
    }
}