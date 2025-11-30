package Users;

import Appointments.Appointment;
import DatabaseManagement.AppointmentService;
import DatabaseManagement.SupabaseClient;
import DatabaseManagement.UserService;


import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Doctor extends User {

    public Doctor(String firstName, String lastName, String email, String password, String specialization) {
        super(firstName, lastName, email, password, specialization);
    }

    @Override
    public Doctor createUser(String email, String firstName, String lastName, String password, String specialization) throws Exception {
        Doctor doctor = new Doctor(email, firstName, lastName, password, specialization);
        int id = UserService.saveUser(doctor, "doctor");
        doctor.setId(id);
        return doctor;
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
