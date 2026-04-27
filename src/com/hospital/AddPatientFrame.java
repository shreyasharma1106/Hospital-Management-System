package com.hospital;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddPatientFrame extends JFrame {

    JTextField nameField, ageField, diseaseField;

    public AddPatientFrame() {
        setTitle("Add Patient");
        setSize(300,250);
        setLayout(new GridLayout(4,2));

        add(new JLabel("Name"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Age"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Disease"));
        diseaseField = new JTextField();
        add(diseaseField);

        JButton save = new JButton("Save");
        add(save);

        save.addActionListener(e -> savePatient());

        setVisible(true);
    }

    void savePatient() {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO patients(name, age, disease) VALUES (?, ?, ?)"
            );

            ps.setString(1, nameField.getText());
            ps.setInt(2, Integer.parseInt(ageField.getText()));
            ps.setString(3, diseaseField.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Patient Added!");

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}