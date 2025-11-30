package Appointments;

import DatabaseManagement.AppointmentService;
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
    private String status;

    // Constructor
    public Appointment(int id, int doctorId, LocalDateTime date, String status) throws Exception {
        this.id = id;
        this.doctor = (Doctor) UserService.fetchUser(doctorId);
        this.date = date;
        this.patient = null;
        this.status = status;
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
    public String getStatus(){return this.status;}

    // Setters
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    public void setPatient(Patient patient) throws Exception {
        this.patient = patient;
        if(patient != null){
            setStatus("SCHEDULED");
        }else{
            setStatus("AVAILABLE");
        }
        AppointmentService.updateAppointment(this);
    }
    public void setDate(LocalDateTime date){
        this.date = date;
    }
    public void setStatus(String status){this.status = status;}

}
