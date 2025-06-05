package view;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoginView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JCheckBox rememberMeCheckBox;

    public LoginView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 460);
        setLocationRelativeTo(null);
        setLayout(null); // Menggunakan absolute layout

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(null);
        leftPanel.setBounds(0, 0, 360, 460);

        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(new Font("Cantarell", Font.BOLD, 36));
        welcomeLabel.setBounds(62, 30, 300, 50);
        leftPanel.add(welcomeLabel);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(40, 100, 100, 20);
        leftPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(40, 125, 280, 30);
        leftPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(40, 170, 100, 20);
        leftPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 195, 280, 30);
        leftPanel.add(passwordField);

        rememberMeCheckBox = new JCheckBox("Remember Me");
        rememberMeCheckBox.setBounds(40, 240, 130, 20);
        leftPanel.add(rememberMeCheckBox);

        JLabel forgotPasswordLabel = new JLabel("Forget Password?");
        forgotPasswordLabel.setBounds(200, 240, 120, 20);
        leftPanel.add(forgotPasswordLabel);

        loginButton = new JButton("Login");
        loginButton.setBounds(40, 280, 280, 40);
        leftPanel.add(loginButton);

        add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setBounds(360, 0, 340, 460);

        JLabel imageLabel = new JLabel();

        // Use the correct relative path for image
        URL imageUrl = getClass().getResource("/img/Love.png");
        if (imageUrl != null) {
            ImageIcon imageIcon = new ImageIcon(imageUrl);
            Image image = imageIcon.getImage().getScaledInstance(320, 320, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(image));
        } else {
            System.err.println("Image not found: /img/Love.png");
            imageLabel.setText("Image not found");
        }

        // Set the image to the label
        imageLabel.setBounds(10, 60, 320, 320);
        rightPanel.add(imageLabel);

        add(rightPanel);

        setVisible(true);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public JButton getLoginButton() {
        dispose();
        return loginButton;

    }
}
