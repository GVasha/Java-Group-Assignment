package DatabaseManagement;

import Authentication.Authentication;
import Users.*;

public class TestBackend {
    public static void main(String[] args) throws Exception {

        // Test create user
        Doctor doctor = Authentication.doctorSingUp("ruben@test.com", "Rubén", "de Juan", "test", "allergy");
        if(doctor == null){
            System.out.println("Existing email");
            return;
        }
        System.out.println("Created user with ID = " + doctor.getId());

        // Test fetch
        String userJson = SupabaseClient.get("User");
        System.out.println("GET result:");
        System.out.println(userJson);

        // Test fetch mapped user
        User fetched = UserService.fetchUser(doctor.getId());
        System.out.println("Fetched user class: " + fetched.getClass().getSimpleName());
    }

}
