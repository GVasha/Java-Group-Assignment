package Users;

import Appointments.Appointment;

import java.util.ArrayList;
import java.util.Date;
import DatabaseManagement.SupabaseClient;

public abstract class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String specialization;
    ArrayList<Appointment> appointments;

    // Constructor
    public User(String email, String firstName, String lastName, String password, String specialization){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.specialization = specialization;
    }

    // Getters
    public int getId(){
        return this.id;
    }
    public String getEmail(){
        return this.email;
    }
    public String getFirstName(){
        return (this.firstName);
    }
    public String getLastName(){
        return (this.lastName);
    }
    public String getName(){
        return (this.firstName + " " + this.lastName);
    }
    public String getPassword(){
        return this.password;
    }
    public String getSpecialization(){
        return this.specialization;
    }
    public ArrayList<Appointment> getAppointments(){
        return this.appointments;
    }

    // Setters
    public void setId(int id){this.id = id;}
    public void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
    public void setSpecialization(String specialization){
        this.specialization = specialization;
    }

    abstract User createUser(String email, String firstName, String lastName, String password, String specialization) throws Exception;
}
