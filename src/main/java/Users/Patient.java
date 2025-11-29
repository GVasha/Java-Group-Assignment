package Users;

import Appointments.Appointment;
import java.util.Date;

import static DatabaseManagement.AppointmentService.fetchAppointment;

public class Patient extends User {
    // Constructor
    public Patient(int id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password);
    }

    public void addAppointment(Date date){
        Date now = new Date();
        Appointment newAppointment = fetchAppointment(1, now); // TODO: How do we want to make users select a doctor? good question ruben
        newAppointment.setPatient(this);
        // TODO: Update it in the database
        this.appointments.add(newAppointment);
    }
}
