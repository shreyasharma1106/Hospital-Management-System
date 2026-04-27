package com.hospital;

import java.sql.*;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("\n--- Hospital System ---");
            System.out.println("1. Add Patient");
            System.out.println("2. View Doctors");
            System.out.println("3. Book Appointment");
            System.out.println("4. Exit");

            int choice = sc.nextInt();

            try {
                Connection con = DBConnection.getConnection();

                switch(choice) {

                    case 1:
                        sc.nextLine();
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter Age: ");
                        int age = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Disease: ");
                        String disease = sc.nextLine();

                        PreparedStatement ps1 = con.prepareStatement(
                            "INSERT INTO patients(name, age, disease) VALUES (?, ?, ?)"
                        );

                        ps1.setString(1, name);
                        ps1.setInt(2, age);
                        ps1.setString(3, disease);

                        ps1.executeUpdate();
                        System.out.println("✅ Patient Added!");
                        break;

                    case 2:
                        ResultSet dr = con.createStatement().executeQuery("SELECT * FROM doctors");

                        while(dr.next()) {
                            System.out.println(
                                dr.getInt("id") + " | " +
                                dr.getString("name") + " | " +
                                dr.getString("specialization")
                            );
                        }
                        break;

                    case 3:
                        sc.nextLine();
                        System.out.print("Enter Patient Name: ");
                        String pname = sc.nextLine();

                        ResultSet doctors = con.createStatement().executeQuery("SELECT * FROM doctors");
                        System.out.println("\nAvailable Doctors:");
                        while(doctors.next()) {
                            System.out.println(
                                doctors.getInt("id") + " - " +
                                doctors.getString("name") + " (" +
                                doctors.getString("specialization") + ")"
                            );
                        }

                        System.out.print("Enter Doctor ID: ");
                        int docId = sc.nextInt();

                        sc.nextLine();
                        System.out.print("Enter Date (YYYY-MM-DD): ");
                        String date = sc.nextLine();

                        System.out.print("Enter Time (HH:MM:SS): ");
                        String time = sc.nextLine();

                        // 🔥 CONFLICT CHECK
                        PreparedStatement check = con.prepareStatement(
                            "SELECT * FROM appointments WHERE doctor_id=? AND date=? AND time=?"
                        );

                        check.setInt(1, docId);
                        check.setString(2, date);
                        check.setString(3, time);

                        ResultSet rs = check.executeQuery();

                        if(rs.next()) {
                            System.out.println("❌ Slot already booked!");
                        } else {

                            PreparedStatement ps2 = con.prepareStatement(
                                "INSERT INTO appointments(patient_name, doctor_id, date, time) VALUES (?, ?, ?, ?)"
                            );

                            ps2.setString(1, pname);
                            ps2.setInt(2, docId);
                            ps2.setString(3, date);
                            ps2.setString(4, time);

                            ps2.executeUpdate();

                            System.out.println("✅ Appointment Booked!");
                        }
                        break;

                    case 4:
                        return;
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}