package Users;

import Appointments.Appointment;

import java.util.ArrayList;
import java.util.Date;

public abstract class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    ArrayList<Appointment> appointments;

    // Constructor
    public User(int id, String email, String firstName, String lastName, String password){
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.password = password;
    }

    // Getters
    public int getId(){
        return this.id;
    }
    public String getEmail(){
        return this.email;
    }
    public String getName(){
        return (this.firstName + " " + this.lastName);
    }
    public String getPassword(){
        return this.password;
    }
    public ArrayList<Appointment> getAppointments(){
        return this.appointments;
    }

    // Setters
    public void setEmail(String email){
        this.email = email;
    }
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
}
