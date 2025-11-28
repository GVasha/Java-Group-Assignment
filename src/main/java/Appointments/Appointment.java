package Appointments;

import Users.Doctor;
import Users.Patient;

import javax.swing.*;
import java.util.Date;

public class Appointment {
    private int id;
    private Doctor doctor;
    private Patient patient;
    private Date date;

    // Constructor
    public Appointment(int id, Doctor doctor, Patient patient, Date date) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
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
    public Date getDate() {
        return this.date;
    }

    // Setters
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public void setDate(Date date){
        this.date = date;
    }

}
