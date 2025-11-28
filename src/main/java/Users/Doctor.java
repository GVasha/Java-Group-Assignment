package Users;

import Appointments.Appointment;

import java.util.Date;

public class Doctor extends User {
    public Doctor(int id, String email, String firstName, String lastName, String password) {
        super(id, email, firstName, lastName, password);
    }

    public Appointment createAppointment(Date date){
        Appointment newAppointment = new Appointment(this, date);
        // Store it in the database
        return newAppointment;
    }
    @Override
    public void addAppointment(Date date){
        Appointment newAppointment = createAppointment(date);
        this.appointments.add(newAppointment);
    }
}
