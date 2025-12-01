package users;

import appointments.Appointment;
import database_management.AppointmentService;
import database_management.DoctorsService;

import java.time.LocalDateTime;
import java.util.List;

public class Doctor extends User {

    public Doctor(int id, String email, String firstName, String lastName, String password, String specialization){
        super(id, email, firstName, lastName, password, specialization);
    }
    // TODO: Do id setting --> Is done in the signup

    public void createAvailableSlot(LocalDateTime dateTime, String notes) throws Exception {
        String json = DoctorsService.createAvailableSlot(this.getId(), dateTime, notes);
    }

    public List<Appointment> getMyAppointments() throws Exception {
        return AppointmentService.fetchAppointmentsByUserId(this.getId(), "doctor");
    }
}
