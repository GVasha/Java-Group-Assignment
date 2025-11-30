package Appointments;

import DatabaseManagement.UserService;
import Users.Doctor;
import Users.Patient;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.Date;

public class Appointment {
    private int id;
    private Doctor doctor;
    private Patient patient;
    private LocalDateTime date;

    // Constructor
    public Appointment(int id, int doctorId, LocalDateTime date) throws Exception {
        this.id = id;
        this.doctor = (Doctor) UserService.fetchUser(doctorId);
        this.date = date;
    }
    public Appointment(int id, int doctorId, int patientId, LocalDateTime date) throws Exception {
        this.id = id;
        this.doctor = (Doctor) UserService.fetchUser(doctorId);
        this.patient = (Patient) UserService.fetchUser(patientId);
        this.date = date;
    }

    // Getters
    public int getId() {
        return this.id;
    }
    public Doctor getDoctor() {
        return this.doctor;
    }
    public Patient getPatient() {
        return this.patient;
    }
    public LocalDateTime getDate() {
        return this.date;
    }

    // Setters
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public void setDate(LocalDateTime date){
        this.date = date;
    }

}
