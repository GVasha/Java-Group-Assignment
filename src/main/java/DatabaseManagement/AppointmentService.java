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


}


