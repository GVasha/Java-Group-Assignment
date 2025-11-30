package utils;
import com.google.gson.JsonObject;
import users.*;

public final class JsonTransformer {
    private JsonTransformer(){}

    // Used for getting the user object from the Supabase endpoint
    public static User jsonToUser(JsonObject obj) throws Exception {
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

    // Private function for filling JSON for saving/updating functionalities
    private static JsonObject fillJson(User user){
        JsonObject json = new JsonObject();
        json.addProperty("first_name", user.getFirstName());
        json.addProperty("last_name", user.getLastName());
        json.addProperty("email", user.getEmail());
        json.addProperty("password", user.getPassword());
        json.addProperty("specialization", user.getSpecialization());

        return json;
    }

    // Turning objects into JSON to send to Supabase
    public static JsonObject userToJson(User user){
        return fillJson(user);
    }

    // Turning objects into JSON to send to Supabase
    public static JsonObject userToJson(User user, String role){
        JsonObject json = fillJson(user);
        json.addProperty("role", role);
        return json;
    }

}
