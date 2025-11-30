package DatabaseManagement;
import Users.*;
import com.google.gson.*;

public class UserService {
    private static final Gson gson = new Gson();

    public static User fetchUser(String email) throws Exception {

        // Fetch by name (match both doctors and patients)
        String json = SupabaseClient.get("User?email=eq." + email);

        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
        if (arr.isEmpty()) return null;

        JsonObject obj = arr.get(0).getAsJsonObject();

        int id = obj.get("id").getAsInt();
        String firstName = obj.get("first_name").getAsString();
        String lastName = obj.get("last_name").getAsString();
        String password = obj.get("password").getAsString();
        String specialization = obj.get("specialization").getAsString();

        // MOST IMPORTANT: check the type column
        String role = obj.get("role").getAsString();

        // Create the correct child class
        return switch (role) {
            case "doctor" -> new Doctor(email, firstName, lastName, password, specialization);
            case "patient" -> new Patient(email, firstName, lastName, password);
            default -> throw new IllegalStateException("Unknown user role: " + role);
        };
    }

    public static User fetchUser(int id) throws Exception {

        // Fetch by ID (match both doctors and patients)
        String json = SupabaseClient.get("User?id=eq." + id);

        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
        if (arr.isEmpty()) return null;

        JsonObject obj = arr.get(0).getAsJsonObject();

        int userId = obj.get("id").getAsInt();
        String email = obj.get("email").getAsString();
        String firstName = obj.get("first_name").getAsString();
        String lastName = obj.get("last_name").getAsString();
        String password = obj.get("password").getAsString();
        String specialization = obj.has("specialization") ? obj.get("specialization").getAsString() : "";

        // MOST IMPORTANT: check the role column
        String role = obj.get("role").getAsString();

        // Create the correct child class
        return switch (role) {
            case "doctor" -> new Doctor(email, firstName, lastName, password, specialization);
            case "patient" -> new Patient(email, firstName, lastName, password);
            default -> throw new IllegalStateException("Unknown user role: " + role);
        };
    }


    public static int saveUser(User user, String role) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("first_name", user.getFirstName());
        json.addProperty("last_name", user.getLastName());
        json.addProperty("email", user.getEmail());
        json.addProperty("password", user.getPassword());
        json.addProperty("role", role);
        json.addProperty("specialization", user.getSpecialization());

        String result = SupabaseClient.post("User", json.toString());

        JsonArray arr = gson.fromJson(result, JsonArray.class);
        if (!arr.isEmpty()) {
            JsonObject insertedUser = arr.get(0).getAsJsonObject();
            int newId = insertedUser.get("id").getAsInt();
            user.setId(newId);
            return newId;
        } else {
            throw new Exception("Failed to insert user, no ID returned.");
        }
    }
    public static int updateUser(User user) throws Exception {
        if (user.getId() <= 0) {
            throw new IllegalArgumentException("User ID must be set to update");
        }

        JsonObject json = new JsonObject();
        json.addProperty("first_name", user.getFirstName());
        json.addProperty("last_name", user.getLastName());
        json.addProperty("email", user.getEmail());
        json.addProperty("password", user.getPassword());
        if (user instanceof Doctor doctor) {
            json.addProperty("specialization", doctor.getSpecialization());
        }

        String result = SupabaseClient.patch("User", user.getId(), json.toString());

        // Return the id of the updated user (should be same as before)
        JsonArray arr = gson.fromJson(result, JsonArray.class);
        if (!arr.isEmpty()) {
            return arr.get(0).getAsJsonObject().get("id").getAsInt();
        } else {
            throw new Exception("Failed to update user, no response returned.");
        }
    }
}
