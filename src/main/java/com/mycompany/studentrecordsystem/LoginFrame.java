/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.studentrecordsystem;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Student Record System Login");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Student Record System Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(44, 62, 80));


        JLabel usernameLabel = new JLabel("Email / Username:");
        usernameField = new JTextField();
        usernameLabel.setForeground(new Color(44, 62, 80));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        passwordLabel.setForeground(new Color(44, 62, 80));
         passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
         
        loginButton = new JButton("Login");
         loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(52, 152, 219));
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(e -> loginUser());
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email/username and password.");
            return;
        }

        if (loginAdmin(username, password)) {
            return;
        }

        JOptionPane.showMessageDialog(this, "Invalid credentials.");
    }

    private boolean loginAdmin(String username, String password) {
        String sql = """
            SELECT admin_id, full_name
            FROM admins
            WHERE username = ? AND password = ? AND status = 'Active'
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String fullName = rs.getString("full_name");
                    JOptionPane.showMessageDialog(this, "Admin login successful. Welcome, " + fullName + "!");
                    dispose();
                    new AdminDashboard().setVisible(true);
                    return true;
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Admin login error: " + ex.getMessage());
        }

        return false;
    }
}