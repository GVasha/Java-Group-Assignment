package database_management;

import appointments.Appointment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static database_management.AppointmentService.fetchAppointments;

public class PatientService {
    public static List<Appointment> fetchAppointmentsForPatientFiltered(int patientId, LocalDateTime start, LocalDateTime end, Integer doctorId, String status) throws Exception {

        Map<String, Object> filters = new HashMap<>();
        filters.put("patient_id", "eq." + patientId);

        if (start != null) {
            filters.put("date_time", "gte." + start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (end != null) {
            filters.put("date_time", "lte." + end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        if (doctorId != null) {
            filters.put("doctor_id", "eq." + doctorId);
        }
        if (status != null) {
            filters.put("status", "eq." + status);
        }

        return fetchAppointments(filters);
    }
}
