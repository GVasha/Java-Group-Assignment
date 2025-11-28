package Authentication;

import Users.*;

import static DatabaseManagement.UserService.fetchUser;

public class Authentication {
    public static User logIn(String email, String password) {
        User auxUser = fetchUser(email); // TODO: add exception if not matching email was found
        if((auxUser != null) && (auxUser.getPassword().equals(password))) {
            return auxUser;
        }
        return null;
    }

    // Returns true if the user could be created and false if not
    public static Patient patientSingUp(String email, String firstName, String lastName, String password){
        if(fetchUser(email) == null){
            Patient patient = new Patient(0 ,email, firstName, lastName, password); // TODO: Decide how to set id (save this instance in the database and check the id assigned by it?)
            return patient;
        }
        return null;
    }

    // TODO: Doctor singUp
}
