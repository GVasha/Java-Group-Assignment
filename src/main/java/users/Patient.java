package users;

import appointments.Appointment;

import java.time.LocalDateTime;
import java.util.List;

import database_management.AppointmentService;


import static database_management.AppointmentService.fetchAppointmentById;

public class Patient extends User {
    // Constructor
    public Patient(int id, String email, String firstName, String lastName, String password) throws Exception {
        super(id, email, firstName, lastName, password, "none");
    }


    public void bookAppointment(Appointment appointment){
        try{
            appointment.setPatient(this);
            appointment.setStatus("SCHEDULED");
            AppointmentService.updateAppointment(appointment);
        }catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void cancelAppointment(Appointment appointment) throws Exception {
        appointment.setPatient(null);
        appointment.setStatus("AVAILABLE");
        AppointmentService.updateAppointment(appointment);
    }

    @Override
    public List<Appointment> getMyAppointments() throws Exception {
        return AppointmentService.fetchAppointmentsByUserId(this.getId(), "patient");
    }

    // TODO: Allow the user to search for available appointments with different parameters (date, speciality, specific doctor)
    // Everything is optional – pass null for filters you don’t want
    public List<Appointment> searchAvailableAppointments(
            LocalDateTime from,
            LocalDateTime to,
            Integer doctorId,
            String specialization
    ) throws Exception {
        return AppointmentService.searchAvailableAppointments(from, to, doctorId, specialization);
    }

}
