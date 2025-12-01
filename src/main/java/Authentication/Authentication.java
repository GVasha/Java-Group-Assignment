package Authentication;

import DatabaseManagement.UserService;
import Users.*;

import static DatabaseManagement.UserService.fetchUser;

public class Authentication {
    public static User logIn(String email, String password) throws Exception {
        User auxUser = fetchUser(email);
        if((auxUser != null) && (auxUser.getPassword().equals(password))) {
            return auxUser;
        }
        return null;
    }

    public static Patient patientSignUp(String email, String firstName, String lastName, String password) throws Exception {
        if(fetchUser(email) == null){
            Patient patient = new Patient(-1, email, firstName, lastName, password);
            int id = UserService.saveUser(patient, "patient");
            patient.setId(id);
            return patient;
        }
        return null;
    }

    public static Doctor doctorSignUp(String email, String firstName, String lastName, String password, String specialization) throws Exception {
        if(fetchUser(email) == null){
            Doctor doctor = new Doctor(-1 ,email, firstName, lastName, password, specialization);
            int id = UserService.saveUser(doctor, "doctor");
            doctor.setId(id);
            return doctor;
        }
        return null;
    }
}
