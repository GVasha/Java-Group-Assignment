package Users;

import Appointments.Appointment;
import java.util.Date;

import DatabaseManagement.AppointmentService;
import DatabaseManagement.SupabaseClient;
import DatabaseManagement.UserService;


import static DatabaseManagement.AppointmentService.fetchAppointment;

public class Patient extends User {
    // Constructor
    public Patient(String email, String firstName, String lastName, String password) throws Exception {
        super(email, firstName, lastName, password, "none");
    }

    public void addAppointment(int appointmentId) throws Exception {
        Appointment newAppointment = fetchAppointment(appointmentId);
        newAppointment.setPatient(this);
        this.appointments.add(newAppointment);
    }
    public void cancelAppointment(Appointment appointment) throws Exception {
        appointment.setPatient(null);
        this.appointments.remove(appointment);
    }
}
