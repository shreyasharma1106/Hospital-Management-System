package com.hospital;
import java.sql.*;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hospital_db",
                "root",
                "1997_pushp@"
            );
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}