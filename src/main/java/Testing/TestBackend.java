package Testing;

import authentication.Authentication;
import database_management.AppointmentService;
import database_management.DoctorsService;
import database_management.SupabaseClient;
import users.*;
import appointments.Appointment;

import java.util.List;

public class TestBackend {
    public static void main(String[] args) {
        System.out.println("==== TEST START ====");

        try {
            // 1️⃣ Login doctor and patient
            Doctor doctor = (Doctor) Authentication.logIn("doc@test.com", "pass123");
            Patient patient = (Patient) Authentication.logIn("pat@test.com", "pass456");
            System.out.println("Logins OK: Doctor ID=" + doctor.getId() + ", Patient ID=" + patient.getId());

            // 2️⃣ Fetch doctor's available slots as JSON
            String rawSlots = DoctorsService.getAvailableSlotsForDoctor(doctor.getId());
            System.out.println("Raw available slots JSON = " + rawSlots);

            // 3️⃣ Map them to Appointment objects
            List<Appointment> docSlots = doctor.getMyAppointments();
            System.out.println("Doctor mapped appointments: " + docSlots.size());
            for (Appointment a : docSlots) {
                System.out.println("Slot ID=" + a.getId() + ", Status=" + a.getStatus() +
                        ", Patient=" + (a.getPatient() != null ? a.getPatient().getId() : "null"));
            }

            if (docSlots.isEmpty()) {
                System.out.println("No available slots to test booking.");
                return;
            }

            // 4️⃣ Patient books the first available slot
            Appointment firstSlot = docSlots.get(0);
            boolean booked = patient.bookAppointment(firstSlot.getId());
            System.out.println("Booking slot ID=" + firstSlot.getId() + " -> " + booked);

            // 5️⃣ Verify patient appointments
            List<Appointment> patientApps = patient.getMyAppointments();
            System.out.println("Patient appointments after booking: " + patientApps.size());
            for (Appointment a : patientApps) {
                System.out.println("Appointment ID=" + a.getId() + ", Status=" + a.getStatus() +
                        ", Doctor=" + a.getDoctor().getId());
            }

            // 6️⃣ Cancel the appointment
            boolean canceled = patient.cancelAppointment(firstSlot.getId());
            System.out.println("Canceling slot ID=" + firstSlot.getId() + " -> " + canceled);

            // 7️⃣ Fetch appointment again to verify cancellation
            Appointment afterCancel = AppointmentService.fetchAppointmentById(firstSlot.getId());
            System.out.println("After cancel -> Status: " + afterCancel.getStatus() +
                    ", Patient=" + (afterCancel.getPatient() != null ? afterCancel.getPatient().getId() : "null"));

            // 8️⃣ Optional: dump database state
            System.out.println("=== ALL USERS ===");
            System.out.println(SupabaseClient.get("User"));

            System.out.println("=== ALL APPOINTMENTS ===");
            System.out.println(SupabaseClient.get("Appointment"));

        } catch (Exception e) {
            System.err.println("Exception occurred during test:");
            e.printStackTrace();
        }

        System.out.println("==== TEST END ====");
    }
}
