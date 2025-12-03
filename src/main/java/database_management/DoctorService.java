package database_management;

// This class is for methods that only doctors can use

import appointments.Appointment;
import com.google.gson.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public static List<Integer> fetchPatientIdsByName(String fullName) throws Exception {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name must not be empty");
        }

        String trimmed = fullName.trim();
        String[] parts = trimmed.split("\\s+");
        if (parts.length < 2) {
            // keep it simple for now: require both first and last name
            throw new IllegalArgumentException("Please enter both first and last name (e.g. \"Pat Ient\")");
        }

        String firstName = parts[0];
        String lastName  = parts[parts.length - 1];

        // Only patients, and only their IDs
        String endpoint = "User"
                + "?role=eq.patient"
                + "&first_name=eq." + firstName
                + "&last_name=eq." + lastName
                + "&select=id";

        String json = SupabaseClient.get(endpoint);
        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

        List<Integer> ids = new ArrayList<>();
        for (JsonElement el : arr) {
            JsonObject obj = el.getAsJsonObject();
            ids.add(obj.get("id").getAsInt());
        }
        return ids;
    }

    public static List<Appointment> fetchAppointmentsForDoctorFiltered(int doctorId, LocalDateTime start, LocalDateTime end, String patientFullName, String status) throws Exception {

        Map<String, Object> filters = new HashMap<>();
        filters.put("doctor_id", "eq." + doctorId);

        if (start != null) {
            filters.put("date_time", "gte." + start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (end != null) {
            filters.put("date_time", "lte." + end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        if (patientFullName != null && !patientFullName.isBlank()) {
            List<Integer> ids = fetchPatientIdsByName(patientFullName);
            if (ids.isEmpty()) {
                return new java.util.ArrayList<>();
            }
            StringBuilder inList = new StringBuilder("in.(");
            for (int i = 0; i < ids.size(); i++) {
                if (i > 0) inList.append(",");
                inList.append(ids.get(i));
            }
            inList.append(")");
            filters.put("patient_id", inList.toString());
        }
        if (status != null) {
            filters.put("status", "eq." + status);
        }

        return fetchAppointments(filters);
    }
}
