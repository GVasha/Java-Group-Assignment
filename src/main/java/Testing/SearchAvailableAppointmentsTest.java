package Testing;

import database_management.AppointmentService;
import appointments.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public class SearchAvailableAppointmentsTest {

    private static void printResult(String label, List<Appointment> apps) {
        System.out.println("\n=== " + label + " ===");
        System.out.println("Found " + apps.size() + " appointments");

        for (Appointment a : apps) {
            // Doctor ID (null-safe)
            Integer doctorId = null;
            if (a.getDoctor() != null) {
                doctorId = a.getDoctor().getId();
            }

            // Patient ID (null-safe)
            Integer patientId = null;
            if (a.getPatient() != null) {
                patientId = a.getPatient().getId();
            }

            // Adjust getDate() / getDateTime() to match your Appointment
            System.out.println(
                    "id=" + a.getId() +
                            ", doctor_id=" + doctorId +
                            ", patient_id=" + patientId +
                            ", date_time=" + a.getDate() +
                            ", status=" + a.getStatus()
            );
        }
    }

    public static void main(String[] args) throws Exception {

        // ---- Scenario 1: all AVAILABLE slots (no filters except status) ----
        List<Appointment> allAvailable =
                AppointmentService.searchAvailableAppointments(
                        null, null, null, null
                );
        printResult("Scenario 1: ALL available (no filters)", allAvailable);

        // ---- Scenario 2: AVAILABLE for doctor 22 (cardiology) ----
        List<Appointment> doc22Available =
                AppointmentService.searchAvailableAppointments(
                        null, null, 22, null
                );
        printResult("Scenario 2: Available for doctor_id = 22", doc22Available);

        // ---- Scenario 3: AVAILABLE for doctor 27 (infectious disease) ----
        List<Appointment> doc27Available =
                AppointmentService.searchAvailableAppointments(
                        null, null, 27, null
                );
        printResult("Scenario 3: Available for doctor_id = 27", doc27Available);

        // ---- Scenario 4: date range for doctor 22 (only 2025-12-02) ----
        LocalDateTime from = LocalDateTime.of(2025, 12, 2, 0, 0);
        LocalDateTime to   = LocalDateTime.of(2025, 12, 2, 23, 59);

        List<Appointment> doc22OnDec2 =
                AppointmentService.searchAvailableAppointments(
                        from, to, 22, null
                );
        printResult("Scenario 4: Doc 22, date range 2025-12-02 only", doc22OnDec2);

        // ---- Scenario 5: specialization only (e.g. cardiology) ----
        List<Appointment> cardioAvailable =
                AppointmentService.searchAvailableAppointments(
                        null, null, null, "cardiology"
                );
        printResult("Scenario 5: Available for specialization = cardiology", cardioAvailable);
    }
}