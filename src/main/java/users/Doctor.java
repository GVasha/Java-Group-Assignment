package users;

import appointments.Appointment;
import database_management.AppointmentService;


import java.time.LocalDateTime;
import java.util.ArrayList;

public class Doctor extends User {

    public Doctor( String email, String firstName, String lastName, String password, String specialization){
        super(email, firstName, lastName, password, specialization);
    }

    public void createAvailableSlot(LocalDateTime dateTime, String notes) throws Exception {
        AppointmentService.createAvailableSlot(this.getId(), dateTime, notes);
    }

    // TODO: Allow doctor to view all their appointments
    public ArrayList<Appointment> getAppointments() {
        // Fetch from database by doctorId
        return null;
    }







}
