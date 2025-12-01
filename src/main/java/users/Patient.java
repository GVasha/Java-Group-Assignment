package users;

import appointments.Appointment;
import java.util.List;

import database_management.AppointmentService;


import static database_management.AppointmentService.fetchAppointmentById;

public class Patient extends User {
    // Constructor
    public Patient(int id, String email, String firstName, String lastName, String password) throws Exception {
        super(id, email, firstName, lastName, password, "none");
    }


    public boolean bookAppointment(int appointmentId) throws Exception {
        Appointment fetchedAppointment = fetchAppointmentById(appointmentId);
        if(fetchedAppointment == null){
            return false;
        }
        fetchedAppointment.setPatient(this);
        fetchedAppointment.setStatus("SCHEDULED");
        AppointmentService.updateAppointment(fetchedAppointment);
        return true;
    }

    @Override
    public boolean cancelAppointment(int appointmentId) throws Exception {
        Appointment fetchedAppointment = fetchAppointmentById(appointmentId);
        if(fetchedAppointment == null){
            return false;
        }
        fetchedAppointment.setPatient(null);
        fetchedAppointment.setStatus("AVAILABLE");
        AppointmentService.updateAppointment(fetchedAppointment);
        return true;
    }

    @Override
    public List<Appointment> getMyAppointments() throws Exception {
        return AppointmentService.fetchAppointmentsByUserId(this.getId(), "patient");
    }

    // TODO: Allow the user to search for available appointments with different parameters (date, speciality, specific doctor)
}
