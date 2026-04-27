package com.hospital;

public class Patient {
    private String name;
    private int age;
    private String disease;
    private String doctor;

    public Patient(String name, int age, String disease, String doctor) {
        this.name = name;
        this.age = age;
        this.disease = disease;
        this.doctor = doctor;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getDisease() { return disease; }
    public String getDoctor() { return doctor; }
}