package appointments;

import database_management.UserService;
import users.Doctor;
import users.Patient;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private Doctor doctor;
    private Patient patient;
    private LocalDateTime date;
    private String status;
    private String notes;

    // Constructor
    public Appointment(int id, int doctorId, int patientId, LocalDateTime date, String status, String notes) throws Exception {
        this.id = id;
        this.doctor = (Doctor) UserService.fetchUser(doctorId);
        this.date = date;
        if(patientId < 0){
            this.patient = null;
        }else {
            this.patient = (Patient) UserService.fetchUser(patientId);
        }
        this.status = status;
        this.notes = notes;
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
    public String getStatus(){
        return this.status;
    }
    public String getNotes(){
        return this.notes;
    }

    // Setters
    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }
    public void setPatient(Patient patient){
        this.patient = patient;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }

}
