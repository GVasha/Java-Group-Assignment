package Users;

import Appointments.Appointment;

import java.util.ArrayList;
import java.util.Date;
import DatabaseManagement.SupabaseClient;
import DatabaseManagement.UserService;

public abstract class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String specialization;

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

    // Setters
    public void setId(int id){this.id = id;}
    public void setEmail(String email) throws Exception {
        this.email = email;
        UserService.updateUser(this);
    }
    public void setPassword(String newPassword) throws Exception {
        this.password = newPassword;
        UserService.updateUser(this);
    }
    public void setSpecialization(String specialization) throws Exception {
        this.specialization = specialization;
        UserService.updateUser(this);
    }
}
