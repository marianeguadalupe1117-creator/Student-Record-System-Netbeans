/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.studentrecordsystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Modern form-driven LoginFrame.
 * The visible layout is inside initComponents(), so NetBeans Design View
 * will show the same dashboard-style login design.
 */
public class LoginFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginFrame.class.getName());

    private final Color navy = new Color(18, 34, 53);
    private final Color blue = new Color(15, 98, 146);
    private final Color blueDark = new Color(10, 75, 115);
    private final Color green = new Color(0, 224, 120);
    private final Color muted = new Color(190, 210, 226);
    private final Color panelBg = new Color(236, 240, 245);

    public LoginFrame() {
        initComponents();
        setupLoginFrame();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rootPanel = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        badgeLabel = new javax.swing.JLabel();
        systemTitleLabel = new javax.swing.JLabel();
        rightPanel = new javax.swing.JPanel();
        welcomeLabel = new javax.swing.JLabel();
        welcomeSubtitleLabel = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        showPasswordCheckBox = new javax.swing.JCheckBox();
        loginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Student Record System Login");
        setMinimumSize(new java.awt.Dimension(900, 560));

        rootPanel.setBackground(new java.awt.Color(236, 240, 245));
        rootPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(28, 31, 28, 31));
        rootPanel.setPreferredSize(new java.awt.Dimension(980, 620));
        rootPanel.setLayout(new java.awt.BorderLayout());

        mainPanel.setBackground(new java.awt.Color(18, 34, 53));
        mainPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        mainPanel.setPreferredSize(new java.awt.Dimension(900, 525));
        mainPanel.setLayout(new java.awt.BorderLayout(38, 0));

        leftPanel.setBackground(new java.awt.Color(18, 34, 53));
        leftPanel.setPreferredSize(new java.awt.Dimension(365, 460));

        badgeLabel.setBackground(new java.awt.Color(8, 30, 58));
        badgeLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        badgeLabel.setForeground(new java.awt.Color(0, 224, 120));
        badgeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        badgeLabel.setText("• SECURE ADMIN PORTAL");
        badgeLabel.setOpaque(true);

        systemTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 42)); // NOI18N
        systemTitleLabel.setForeground(new java.awt.Color(255, 255, 255));
        systemTitleLabel.setText("<html>  Student Record<br>System</html>");

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(badgeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(systemTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addComponent(badgeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(systemTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(256, Short.MAX_VALUE))
        );

        mainPanel.add(leftPanel, java.awt.BorderLayout.WEST);

        rightPanel.setBackground(new java.awt.Color(236, 240, 245));
        rightPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(42, 34, 24, 34));
        rightPanel.setPreferredSize(new java.awt.Dimension(465, 475));

        welcomeLabel.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        welcomeLabel.setForeground(new java.awt.Color(15, 23, 42));
        welcomeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcomeLabel.setText("Welcome Back");

        welcomeSubtitleLabel.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        welcomeSubtitleLabel.setForeground(new java.awt.Color(100, 116, 139));
        welcomeSubtitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcomeSubtitleLabel.setText("Sign in to continue to the Admin Dashboard");

        usernameLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        usernameLabel.setForeground(new java.awt.Color(15, 23, 42));
        usernameLabel.setText("Email / Username");

        usernameField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usernameField.setForeground(new java.awt.Color(15, 23, 42));
        usernameField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(15, 98, 146)));
        usernameField.setCaretColor(new java.awt.Color(15, 98, 146));

        passwordLabel.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        passwordLabel.setForeground(new java.awt.Color(15, 23, 42));
        passwordLabel.setText("Password");

        passwordField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passwordField.setForeground(new java.awt.Color(15, 23, 42));
        passwordField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(203, 213, 225)));
        passwordField.setCaretColor(new java.awt.Color(15, 98, 146));

        showPasswordCheckBox.setBackground(new java.awt.Color(236, 240, 245));
        showPasswordCheckBox.setForeground(new java.awt.Color(71, 85, 105));
        showPasswordCheckBox.setText("Show password");
        showPasswordCheckBox.setFocusPainted(false);

        loginButton.setBackground(new java.awt.Color(15, 98, 146));
        loginButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("LOGIN ");
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(this::loginButtonActionPerformed);

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(welcomeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(welcomeSubtitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
            .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(usernameField)
            .addComponent(passwordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(passwordField)
            .addComponent(showPasswordCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(loginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(welcomeSubtitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(passwordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(showPasswordCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );

        mainPanel.add(rightPanel, java.awt.BorderLayout.CENTER);

        rootPanel.add(mainPanel, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rootPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loginButtonActionPerformed

    private void setupLoginFrame() {
        setSize(980, 620);
        setLocationRelativeTo(null);

        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(blue, 2),
                new EmptyBorder(9, 12, 9, 12)
        ));

        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                new EmptyBorder(10, 12, 10, 12)
        ));

        passwordField.setEchoChar('•');

        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(new EmptyBorder(12, 20, 12, 20));

        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (loginButton.isEnabled()) {
                    loginButton.setBackground(blueDark);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (loginButton.isEnabled()) {
                    loginButton.setBackground(blue);
                }
            }
        });

        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        usernameField.addActionListener(e -> passwordField.requestFocusInWindow());
        passwordField.addActionListener(e -> loginUser());
        loginButton.addActionListener(e -> loginUser());
        getRootPane().setDefaultButton(loginButton);
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
          
            JOptionPane.showMessageDialog(this, "Please enter email/username and password.");
            return;
        }

        setLoading(true);

        SwingUtilities.invokeLater(() -> {
            try {
                if (loginAdmin(username, password)) {
                    return;
                }

                JOptionPane.showMessageDialog(this, "Invalid credentials.");
            } finally {
                setLoading(false);
            }
        });
    }

    private void setLoading(boolean loading) {
        loginButton.setEnabled(!loading);
        usernameField.setEnabled(!loading);
        passwordField.setEnabled(!loading);
        showPasswordCheckBox.setEnabled(!loading);

        if (loading) {
            loginButton.setText("CHECKING...");
            loginButton.setBackground(new Color(100, 116, 139));
        } else {
            loginButton.setText("LOGIN   →");
            loginButton.setBackground(blue);
        }
    }

    private boolean loginAdmin(String username, String password) {
        String sql = "SELECT admin_id, full_name "
                + "FROM admins "
                + "WHERE username = ? AND password = ? AND status = 'Active'";

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

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel badgeLabel;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JButton loginButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JCheckBox showPasswordCheckBox;
    private javax.swing.JLabel systemTitleLabel;
    private javax.swing.JTextField usernameField;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JLabel welcomeLabel;
    private javax.swing.JLabel welcomeSubtitleLabel;
    // End of variables declaration//GEN-END:variables
}
