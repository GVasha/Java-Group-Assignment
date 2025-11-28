package DatabaseManagement;

import Appointments.Appointment;
import Users.*;
import com.google.gson.*;

import java.util.Date;

public class AppointmentService {

    private static final UserService userService = new UserService();   // unified user fetcher

    public static Appointment fetchAppointment(int doctorId, Date targetDate) throws Exception {

        // 1. Fetch all appointment links for this doctor
        String jsonLinks = SupabaseClient.get("appointment_users?user_id=eq." + doctorId);
        JsonArray linkArr = JsonParser.parseString(jsonLinks).getAsJsonArray();
        if (linkArr.isEmpty()) return null;

        // 2. Iterate over each appointment linked to the doctor
        for (JsonElement el : linkArr) {

            JsonObject linkObj = el.getAsJsonObject();
            int appointmentId = linkObj.get("appointment_id").getAsInt();

            // 3. Fetch the appointment details
            String jsonAppointment = SupabaseClient.get("appointments?id=eq." + appointmentId);
            JsonArray appArr = JsonParser.parseString(jsonAppointment).getAsJsonArray();
            if (appArr.isEmpty()) continue;

            JsonObject appObj = appArr.get(0).getAsJsonObject();

            // Parse the appointment date (adjust depending on your DB format)
            Date appointmentDate = new Date(appObj.get("date").getAsLong());

            // 4. Check if date matches the target
            if (!appointmentDate.equals(targetDate)) {
                continue;
            }

            // 5. Fetch the doctor (already known)
            User userDoctor = userService.fetchUserById(doctorId);
            Doctor doctor = (Doctor) userDoctor;

            // 6. Fetch the patient linked to this appointment
            Patient patient = null;

            // fetch all users for this appointment
            String jsonUsers = SupabaseClient.get("appointment_users?appointment_id=eq." + appointmentId);
            JsonArray usersArr = JsonParser.parseString(jsonUsers).getAsJsonArray();

            for (JsonElement ue : usersArr) {
                JsonObject u = ue.getAsJsonObject();
                int uid = u.get("user_id").getAsInt();

                if (uid == doctorId) continue; // Skip doctor, already handled

                User fetched = userService.fetchUserById(uid);
                if (fetched instanceof Patient) {
                    patient = (Patient) fetched;
                }
            }

            // 7. Build and return the appointment
            return new Appointment(appointmentId, doctor, patient, appointmentDate);
        }

        return null;
    }


}


