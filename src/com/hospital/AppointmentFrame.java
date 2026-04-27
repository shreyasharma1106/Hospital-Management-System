package com.hospital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class AppointmentFrame extends JFrame {

    JTextField patientField, dateField, timeField;
    JComboBox<String> doctorBox;

    public AppointmentFrame() {

        setTitle("Book Appointment");
        setSize(450, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 🌟 Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 247, 250));

        // Fields
        patientField = new JTextField();
        dateField = new JTextField("YYYY-MM-DD");
        timeField = new JTextField("HH:MM:SS");

        doctorBox = new JComboBox<>();
        loadDoctors(); // load doctors from DB

        // Labels + Fields
        panel.add(new JLabel("Patient Name:"));
        panel.add(patientField);

        panel.add(new JLabel("Doctor:"));
        panel.add(doctorBox);

        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);

        panel.add(new JLabel("Time (HH:MM:SS):"));
        panel.add(timeField);

        // Button
        JButton bookBtn = new JButton("Book Appointment");
        bookBtn.setBackground(new Color(52, 152, 219));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFocusPainted(false);

        // Layout
        add(panel, BorderLayout.CENTER);
        add(bookBtn, BorderLayout.SOUTH);

        // Action
        bookBtn.addActionListener(e -> bookAppointment());

        setVisible(true);
    }

    // 🔥 LOAD DOCTORS WITH SPECIALIZATION
    void loadDoctors() {
        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!");
                return;
            }

            doctorBox.removeAllItems(); // important

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM doctors");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String spec = rs.getString("specialization");

                String full = id + " - " + name + " (" + spec + ")";
                doctorBox.addItem(full);

                System.out.println(full); // debug
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🚫 BOOK APPOINTMENT WITH CONFLICT CHECK
    void bookAppointment() {
        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!");
                return;
            }

            String patient = patientField.getText();
            String selected = doctorBox.getSelectedItem().toString();

            int doctorId = Integer.parseInt(selected.split(" - ")[0]);

            String date = dateField.getText();
            String time = timeField.getText();

            // 🔍 CHECK IF SLOT ALREADY BOOKED
            PreparedStatement check = con.prepareStatement(
                "SELECT * FROM appointments WHERE doctor_id=? AND date=? AND time=?"
            );

            check.setInt(1, doctorId);
            check.setString(2, date);
            check.setString(3, time);

            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "❌ Slot already booked!\nChoose another time.");
                return;
            }

            // ✅ INSERT APPOINTMENT
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO appointments(patient_name, doctor_id, date, time) VALUES (?, ?, ?, ?)"
            );

            ps.setString(1, patient);
            ps.setInt(2, doctorId);
            ps.setString(3, date);
            ps.setString(4, time);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Appointment Booked!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}