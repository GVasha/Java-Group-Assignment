package Users;

import Appointments.Appointment;
import java.util.Date;

import DatabaseManagement.AppointmentService;
import DatabaseManagement.SupabaseClient;
import DatabaseManagement.UserService;


import static DatabaseManagement.AppointmentService.fetchAppointmentById;

public class Patient extends User {
    // Constructor
    public Patient(String email, String firstName, String lastName, String password) throws Exception {
        super(email, firstName, lastName, password, "none");
    }

    public void bookAppointment(int appointmentId) throws Exception {
        Appointment fetchedAppointment = fetchAppointmentById(appointmentId);
        fetchedAppointment.setPatient(this);
    }
    public void cancelAppointment(Appointment appointment) throws Exception {
        appointment.setPatient(null);
    }
}
