package database_management;

// This class is for methods that only doctors can use

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;

public class DoctorsService {
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
}
