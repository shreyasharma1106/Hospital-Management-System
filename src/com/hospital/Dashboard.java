package com.hospital;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Dashboard");
        setSize(300,200);
        setLayout(new FlowLayout());

        JButton addPatient = new JButton("Add Patient");
        JButton bookApp = new JButton("Book Appointment");

        add(addPatient);
        add(bookApp);

        addPatient.addActionListener(e -> new AddPatientFrame());
        bookApp.addActionListener(e -> new AppointmentFrame());

        setVisible(true);
    }
}