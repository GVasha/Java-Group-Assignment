package utils;
import com.google.gson.JsonObject;
import users.*;

public final class JsonTransformer {
    private JsonTransformer(){}

    public static User jsonObjToUser(JsonObject obj) throws Exception {

        String email = obj.get("email").getAsString();
        String firstName = obj.get("first_name").getAsString();
        String lastName = obj.get("last_name").getAsString();
        String password = obj.get("password").getAsString();
        String specialization = obj.get("specialization").getAsString();

        String role = obj.get("role").getAsString();

        return switch (role) {
            case "doctor" -> new Doctor(email, firstName, lastName, password, specialization);
            case "patient" -> new Patient(email, firstName, lastName, password);
            default -> throw new IllegalStateException("Unknown user role: " + role);
        };
    }

}
