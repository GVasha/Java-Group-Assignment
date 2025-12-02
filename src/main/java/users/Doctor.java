package users;

import appointments.Appointment;
import database_management.AppointmentService;
import database_management.DoctorsService;

import java.time.LocalDateTime;
import java.util.List;

import static database_management.AppointmentService.fetchAppointmentById;



public class Doctor extends User {

    public Doctor(int id, String email, String firstName, String lastName, String password, String specialization){
        super(id, email, firstName, lastName, password, specialization);
    }

    public void createAvailableSlot(LocalDateTime dateTime, String notes) throws Exception {
        String json = DoctorsService.createAvailableSlot(this.getId(), dateTime, notes);
    }

    // When a patient is correctly served, the doctor marks the appointment as completed
    public boolean completeAppointment(int appointmentId) throws Exception {
        Appointment fetchedAppointment = fetchAppointmentById(appointmentId);
        if(fetchedAppointment == null){
            return false;
        }
        fetchedAppointment.setStatus("COMPLETED");
        AppointmentService.updateAppointment(fetchedAppointment);
        return true;
    }

    @Override
    public boolean cancelAppointment(int appointmentId) throws Exception {
        Appointment fetchedAppointment = fetchAppointmentById(appointmentId);
        if(fetchedAppointment == null){
            return false;
        }
        fetchedAppointment.setStatus("CANCELLED");
        AppointmentService.updateAppointment(fetchedAppointment);
        return true;
    }

    @Override
    public List<Appointment> getMyAppointments() throws Exception {
        return AppointmentService.fetchAppointmentsByUserId(this.getId(), "doctor");
    }
}
