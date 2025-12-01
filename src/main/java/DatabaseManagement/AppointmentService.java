package DatabaseManagement;

import Appointments.Appointment;
import Users.*;
import com.google.gson.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentService {
    private static final Gson gson = new Gson();


    public static List<Appointment> fetchAppointments(Map<String, Object> filters) throws Exception {

        if (filters == null || filters.isEmpty())
            throw new IllegalArgumentException("At least one filter must be provided.");

        // Build query dynamically
        StringBuilder query = new StringBuilder("Appointment?");

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            query.append(entry.getKey())
                    .append("=eq.")
                    .append(entry.getValue())
                    .append("&");
        }

        // Remove last "&"
        query.setLength(query.length() - 1);

        String json = SupabaseClient.get(query.toString());
        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

        List<Appointment> result = new ArrayList<>();

        for (JsonElement elem : arr) {
            JsonObject obj = elem.getAsJsonObject();

            int id = obj.get("id").getAsInt();
            int doctorId = obj.get("doctor_id").getAsInt();
            int patientId;
            if(obj.get("patient_id").isJsonNull()) {
                patientId = -1; // -1 indicates no patient
            }else{
                patientId = obj.get("patient_id").getAsInt();
            }
            String dateTimeStr = obj.get("date_time").getAsString(); // e.g. "2025-11-30T14:00:00"
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String status = obj.get("status").getAsString();

            Appointment appointment = new Appointment(id, doctorId, patientId, dateTime, status);

            result.add(appointment);
        }

        return result;
    }

    public static Appointment fetchAppointmentById(int appointmentId) throws Exception {
        Map<String, Object> filters = new HashMap<>();
        filters.put("id", appointmentId);

        List<Appointment> results = fetchAppointments(filters);

        return results.isEmpty() ? null : results.get(0);
    }

    public static List<Appointment> fetchAppointmentsByUserId(int userId, String role) throws Exception {

        if (!role.equals("doctor") && !role.equals("patient")) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put(role + "_id", userId);

        return fetchAppointments(filters);
    }


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

    public static int updateAppointment(Appointment appointment) throws Exception {
        if (appointment.getId() <= 0) {
            throw new IllegalArgumentException("Appointment ID must be set to update");
        }

        JsonObject json = new JsonObject();
        Patient patient = appointment.getPatient();
        if(patient != null){
            json.addProperty("patient_id", patient.getId());
        }else{
            json.addProperty("patient_id", -1);
        }
        json.addProperty("status", appointment.getStatus());

        String result = SupabaseClient.patch("Appointment", appointment.getId(), json.toString());

        // Return the id of the updated appointment (should be same as before)
        JsonArray arr = gson.fromJson(result, JsonArray.class);
        if (!arr.isEmpty()) {
            return arr.get(0).getAsJsonObject().get("id").getAsInt();
        } else {
            throw new Exception("Failed to update appointment, no response returned.");
        }
    }


}


