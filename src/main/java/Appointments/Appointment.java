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
    public Appointment(int id, int doctorId, int patientId, LocalDateTime date, String status) throws Exception {
        this.id = id;
        this.doctor = (Doctor) UserService.fetchUser(doctorId);
        this.date = date;
        if(patientId < 0){
            this.patient = null;
        }else {
            this.patient = (Patient) UserService.fetchUser(patientId);
        }
        this.status = status;
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
    public void setDoctor(Doctor doctor) throws Exception {
        this.doctor = doctor;
        AppointmentService.updateAppointment(this);
    }
    public void setPatient(Patient patient) throws Exception {
        this.patient = patient;
        if(patient != null){
            setStatus("SCHEDULED");
        }else{
            setStatus("AVAILABLE");
        }
    }
    public void setDate(LocalDateTime date) throws Exception {
        this.date = date;
        AppointmentService.updateAppointment(this);
    }
    public void setStatus(String status) throws Exception {
        this.status = status;
        AppointmentService.updateAppointment(this);}

}
