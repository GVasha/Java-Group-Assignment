package DatabaseManagement;
import Users.*;
import com.google.gson.*;

public class UserService {
    private final Gson gson = new Gson();

    public static User fetchUser(String email) throws Exception {

        // Fetch by name (match both doctors and patients)
        String json = SupabaseClient.get("users?email=eq." + email);

        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
        if (arr.isEmpty()) return null;

        JsonObject obj = arr.get(0).getAsJsonObject();

        int id = obj.get("id").getAsInt();
        String firstName = obj.get("first_name").getAsString();
        String lastName = obj.get("last_name").getAsString();
        String password = obj.get("password").getAsString();

        // MOST IMPORTANT: check the type column
        String type = obj.get("type").getAsString();

        // Create the correct child class
        return switch (type) {
            case "Doctor" -> new Doctor(id, email, firstName, lastName, password);
            case "Patient" -> new Patient(id, email, firstName, lastName, password);
            default -> throw new IllegalStateException("Unknown user type: " + type);
        };
    }

    public User fetchUser(int id) throws Exception {
        String json = SupabaseClient.get("users?id=eq." + id);

        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
        if (arr.isEmpty()) return null;

        JsonObject obj = arr.get(0).getAsJsonObject();

        int userId = obj.get("id").getAsInt();
        String email = obj.get("email").getAsString();
        String firstName = obj.get("first_name").getAsString();
        String lastName = obj.get("last_name").getAsString();
        String password = obj.get("password").getAsString();
        String type = obj.get("type").getAsString();

        switch (type) {
            case "Doctor":
                return new Doctor(userId, email, firstName, lastName, password);
            case "Patient":
                return new Patient(userId, email, firstName, lastName, password);
            default:
                throw new IllegalStateException("Unknown user type: " + type);
        }
    }

    public User fetchUserById(int uid) {
    }
}
