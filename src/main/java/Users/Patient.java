package Users;

import Appointments.Appointment;
import java.util.Date;

import static DatabaseManagement.AppointmentService.fetchAppointment;

public class Patient extends User {
    // Constructor
    public Patient(int id, String email, String firstName, String lastName, String password) {
        super(id, email, firstName, lastName, password);
    }

    public void addAppointment(Date date){
        Date now = new Date();
        Appointment newAppointment = fetchAppointment(1, now); // How do we want to make users select a doctor?
        newAppointment.setPatient(this);
        // Update it in the database
        this.appointments.add(newAppointment);
    }
}
