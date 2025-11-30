package Authentication;

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

    public static Patient patientSingUp(String email, String firstName, String lastName, String password) throws Exception {
        if(fetchUser(email) == null){
            return new Patient(email, firstName, lastName, password);
        }
        return null;
    }

    public static Doctor doctorSingUp(String email, String firstName, String lastName, String password, String specialization) throws Exception {
        if(fetchUser(email) == null){
            return new Doctor(email, firstName, lastName, password, specialization);
        }
        return null;
    }
}
