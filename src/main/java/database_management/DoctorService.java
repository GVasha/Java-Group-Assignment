package database_management;

// This class is for methods that only doctors can use

import appointments.Appointment;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static database_management.AppointmentService.fetchAppointments;

public class DoctorService {
    // For doctors only: to create available slots
    public static String createAvailableSlot(int doctorId, LocalDateTime dateTime, String notes) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("doctor_id", doctorId);
        body.add("patient_id", JsonNull.INSTANCE);         // must be NULL for AVAILABLE
        body.addProperty("date_time", dateTime.toString()); // 2025-12-01T10:00
        body.addProperty("status", "AVAILABLE");
        body.addProperty("notes", notes == null ? "" : notes);

        return SupabaseClient.post("Appointment", body.toString());
    }

    //For doctors only: to fetch doctors available slots
    public static String getAvailableSlotsForDoctor(int doctorId) throws Exception {
        String endpoint = "Appointment?doctor_id=eq." + doctorId + "&status=eq.AVAILABLE";
        return SupabaseClient.get(endpoint);
    }

    public static List<Appointment> fetchAppointmentsForDoctorFiltered(int doctorId, LocalDateTime start, LocalDateTime end, String patientFullName) throws Exception {

        Map<String, Object> filters = new HashMap<>();
        filters.put("doctor_id", "eq." + doctorId);

        if (start != null) {
            filters.put("date_time", "gte." + start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (end != null) {
            filters.put("date_time", "lte." + end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        if (patientFullName != null && !patientFullName.isBlank()) {
            // Resolve name -> user IDs
            List<Integer> ids = UserService.fetchUserIdsByName(patientFullName);
            if (ids.isEmpty()) {
                return new java.util.ArrayList<>();
            }
            // Build IN filter
            StringBuilder inList = new StringBuilder("in.(");
            for (int i = 0; i < ids.size(); i++) {
                if (i > 0) inList.append(",");
                inList.append(ids.get(i));
            }
            inList.append(")");
            filters.put("patient_id", inList.toString());
        }

        return fetchAppointments(filters);
    }
}
