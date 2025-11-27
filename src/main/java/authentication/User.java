package authentication;

import java.util.ArrayList;

public class User {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private ArrayList<Appointment> appointments;

    public User(String email, String firstName, String lastName, String password){
        this.email = email;
        this.firstName = firstName;
        this.password = password;
    }
    public ArrayList<Appointment> getAppointments(){
        return this.appointments;
    }
    public void addAppointment(){

    }
}
