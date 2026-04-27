package com.hospital;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    JTextField userField;
    JPasswordField passField;

    // ✅ CONSTRUCTOR (you were missing this)
    public LoginFrame() {

        setTitle("Hospital Login");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,1,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        userField = new JTextField();
        passField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Signup");

        panel.add(new JLabel("Username"));
        panel.add(userField);

        panel.add(new JLabel("Password"));
        panel.add(passField);

        JPanel btnPanel = new JPanel();
        btnPanel.add(loginBtn);
        btnPanel.add(signupBtn);

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> login());
        signupBtn.addActionListener(e -> signup());

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // 🔐 LOGIN
    void login() {
        try {
            Connection con = DBConnection.getConnection();

            if(con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!");
                return;
            }

            String user = userField.getText();
            String pass = new String(passField.getPassword());

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?"
            );

            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new Dashboard();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login!");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 🆕 SIGNUP
    void signup() {
        try {
            Connection con = DBConnection.getConnection();

            if(con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!");
                return;
            }

            String user = userField.getText();
            String pass = new String(passField.getPassword());

            // check existing user
            PreparedStatement check = con.prepareStatement(
                "SELECT * FROM users WHERE username=?"
            );
            check.setString(1, user);

            ResultSet rs = check.executeQuery();

            if(rs.next()) {
                JOptionPane.showMessageDialog(this, "User already exists!");
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO users(username, password) VALUES (?, ?)"
            );

            ps.setString(1, user);
            ps.setString(2, pass);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Signup Successful! Now login.");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}