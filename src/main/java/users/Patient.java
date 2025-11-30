package users;

import appointments.Appointment;


import static database_management.AppointmentService.fetchAppointment;

public class Patient extends User {
    // Constructor
    public Patient(String email, String firstName, String lastName, String password) throws Exception {
        super(email, firstName, lastName, password, "none");
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
}
