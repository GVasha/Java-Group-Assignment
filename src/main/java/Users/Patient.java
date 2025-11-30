package Users;

import Appointments.Appointment;
import java.util.Date;

import DatabaseManagement.AppointmentService;
import DatabaseManagement.SupabaseClient;
import DatabaseManagement.UserService;


import static DatabaseManagement.AppointmentService.fetchAppointment;

public class Patient extends User {
    // Constructor
    public Patient(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, "none");
    }

    public String bookAppointment(int appointmentId) throws Exception {
        return AppointmentService.bookAppointment(appointmentId, this.getId());
    }

    public void cancelAppointment(Appointment appointment){
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
