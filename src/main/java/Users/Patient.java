package Users;

import Appointments.Appointment;
import java.util.Date;
import DatabaseManagement.SupabaseClient;
import DatabaseManagement.UserService;


import static DatabaseManagement.AppointmentService.fetchAppointment;

public class Patient extends User {
    // Constructor
    public Patient(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, "none");
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

    @Override
    public Patient createUser(String email, String firstName, String lastName, String password, String specialization) throws Exception {
        // Specialization does not matter, it will always be = "none"
        Patient patient = new Patient(email, firstName, lastName, password);
        int id = UserService.saveUser(patient, "patient");
        patient.setId(id);
        return patient;
    }

}
