package database_management;

import appointments.Appointment;
import users.*;
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

        StringBuilder query = new StringBuilder("Appointment?"); // Dynamic query

        for (Map.Entry<String, Object> entry : filters.entrySet()) {
            query.append(entry.getKey())
                    .append("=eq.")
                    .append(entry.getValue())
                    .append("&");
        }

        query.setLength(query.length() - 1); // Remove the last '&'

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
            String notes = obj.get("notes").getAsString();

            Appointment appointment = new Appointment(id, doctorId, patientId, dateTime, status, notes);

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

    public static int updateAppointment(Appointment appointment) throws Exception {
        if (appointment.getId() <= 0) {
            throw new IllegalArgumentException("Appointment ID must be set to update");
        }

        JsonObject json = new JsonObject();

        // Use the same "style" as your parsing code
        int doctorId = appointment.getDoctor().getId();
        json.addProperty("doctor_id", doctorId);

        LocalDateTime date = appointment.getDate();
        json.addProperty("date_time", date.toString());

        String status = appointment.getStatus();
        json.addProperty("status", status);

        String notes = appointment.getNotes();
        json.addProperty("notes", notes == null ? "" : notes);

        Patient patient = appointment.getPatient();
        if (patient != null) {
            int patientId = patient.getId();
            json.addProperty("patient_id", patientId);
        } else {
            json.add("patient_id", JsonNull.INSTANCE);
        }

        String result = SupabaseClient.patch("Appointment", appointment.getId(), json.toString());

        JsonArray arr = JsonParser.parseString(result).getAsJsonArray();
        if (!arr.isEmpty()) {
            JsonObject obj = arr.get(0).getAsJsonObject();
            int id = obj.get("id").getAsInt();
            return id;
        } else {
            throw new Exception("Failed to update appointment, no response returned.");
        }
    }


}


