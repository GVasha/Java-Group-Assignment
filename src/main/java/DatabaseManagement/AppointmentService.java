package DatabaseManagement;

import Appointments.Appointment;
import Users.*;
import com.google.gson.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AppointmentService {
    // unified user fetcher

    public static Appointment fetchAppointment(int appointmentId) throws Exception{
        String json = SupabaseClient.get("appointment?id=eq." + appointmentId);

        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
        if (arr.isEmpty()) return null;

        JsonObject obj = arr.get(0).getAsJsonObject();

        int id = obj.get("id").getAsInt();
        int doctorId = obj.get("doctor_id").getAsInt();
        int patientId = obj.get("patient_id").getAsInt();
        String dateTimeStr = obj.get("date_time").getAsString(); // e.g. "2025-11-30T14:00:00"
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        if(patientId < 0){
            return new Appointment(id, doctorId, dateTime);
        }else{
            return new Appointment(id, doctorId, patientId, dateTime);
        }

    }

    // For doctors only: to create available slots
    public static String createAvailableSlot(int doctorId, LocalDateTime dateTime, String notes) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("doctor_id", doctorId);
        body.add("patient_id", JsonNull.INSTANCE);         // must be NULL for AVAILABLE
        body.addProperty("date_time", dateTime.toString()); // 2025-12-01T10:00
        body.addProperty("status", "AVAILABLE");
        body.addProperty("notes", notes == null ? "" : notes);

        // hits /rest/v1/Appointment
        return SupabaseClient.post("Appointment", body.toString());
    }

    //For doctors only: to fetch doctors available slots
    public static String getAvailableSlotsForDoctor(int doctorId) throws Exception {
        String endpoint = "Appointment?doctor_id=eq." + doctorId + "&status=eq.AVAILABLE";
        return SupabaseClient.get(endpoint);
    }

    //For patients: book an AVAILABLE slot (set patient_id + change status)
    public static String bookAppointment(int appointmentId, int patientId) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("patient_id", patientId);
        body.addProperty("status", "BOOKED");   // or "CONFIRMED", just be consistent

        // this will hit: /rest/v1/Appointment?id=eq.<appointmentId>
        return SupabaseClient.patch("Appointment", appointmentId, body.toString());
    }



}


