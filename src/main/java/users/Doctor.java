package users;

import appointments.Appointment;
import database_management.AppointmentService;
import database_management.DoctorService;

import java.time.LocalDateTime;
import java.util.List;

import static database_management.AppointmentService.fetchAppointmentById;



public class Doctor extends User {

    public Doctor(int id, String email, String firstName, String lastName, String password, String specialization){
        super(id, email, firstName, lastName, password, specialization);
    }

    public void createAvailableSlot(LocalDateTime dateTime, String notes) throws Exception {
        String json = DoctorService.createAvailableSlot(this.getId(), dateTime, notes);
    }

    // When a patient is correctly served, the doctor marks the appointment as completed
    public void completeAppointment(Appointment appointment){
        try {
            appointment.setStatus("COMPLETED");
            AppointmentService.updateAppointment(appointment);
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void cancelAppointment(Appointment appointment){
        try {
            appointment.setStatus("CANCELLED");
            AppointmentService.updateAppointment(appointment);

        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<Appointment> getMyAppointments() throws Exception {
        return AppointmentService.fetchAppointmentsByUserId(this.getId(), "doctor");
    }

    public List<Appointment> getMyAppointments(LocalDateTime start, LocalDateTime end, String patientFullName, String status) throws Exception {
        return DoctorService.fetchAppointmentsForDoctorFiltered(this.getId(), start, end, patientFullName, status);
    }

    // This function eliminates the current patient and makes the appointment available again
    public void makeAppointmentAvailable(Appointment appointment){
        try {
            appointment.setPatient(null);
            appointment.setStatus("AVAILABLE");
            AppointmentService.updateAppointment(appointment);

        }catch(Exception exception){
            throw new RuntimeException(exception);
        }
    }

    public void deleteAppointment(Appointment appointment){
        try {
            AppointmentService.deleteAppointment(appointment.getId());
        }catch(Exception exception){
            throw new RuntimeException(exception);
        }
    }
}
