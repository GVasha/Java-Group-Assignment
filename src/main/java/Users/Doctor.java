package Users;

import Appointments.Appointment;
import DatabaseManagement.SupabaseClient;


import java.util.Date;

public class Doctor extends User {
    public Doctor(int id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password);
    }

    // TODO: Allow doctor to create a schedule slot (availability)
    public void createScheduleSlot(LocalDateTime start, LocalDateTime end) {
        // Implementation planned later
    }

    // TODO: Allow doctor to create a schedule slot (availability)
    public void createScheduleSlot(LocalDateTime start, LocalDateTime end) {
        // Implementation planned later
    }

    // TODO: Allow doctor to view all their appointments
    public List<Appointment> getAppointments() {
        // Fetch from database by doctorId
        return null;
    }






}
