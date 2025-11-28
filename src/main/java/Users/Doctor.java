package Users;

import Appointments.Appointment;

import java.util.Date;

public class Doctor extends User {
    public Doctor(int id, String email, String firstName, String lastName, String password) {
        super(id, email, firstName, lastName, password);
    }

    public Appointment createAppointment(Date date){
        // TODO: Check if the date is valid
        Appointment newAppointment = new Appointment(0, this, null, date);
        // TODO: Store it in the database
        return newAppointment;
    }

    public void addAppointment(Date date){
        Appointment newAppointment = createAppointment(date);
        this.appointments.add(newAppointment);
    }
}
