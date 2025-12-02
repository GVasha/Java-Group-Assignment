package database_management;
import users.*;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;
import utils.JsonTransformer;

public class UserService {
    private static final Gson gson = new Gson();

    public static User fetchUser(String email) throws Exception {

        // Fetch by name (match both doctors and patients)
        String json = SupabaseClient.get("User?email=eq." + email);

        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
        if (arr.isEmpty()) return null;

        JsonObject obj = arr.get(0).getAsJsonObject();

        return JsonTransformer.jsonToUser(obj);
    }

    public static User fetchUser(int id) throws Exception {

        // Fetch by ID (match both doctors and patients)
        String json = SupabaseClient.get("User?id=eq." + id);

        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();
        if (arr.isEmpty()) return null;

        JsonObject obj = arr.get(0).getAsJsonObject();

        return JsonTransformer.jsonToUser(obj);
    }

    public static List<Doctor> fetchDoctors(String specialization) throws Exception {
        String json = SupabaseClient.get("User?specialization=eq." + specialization);

        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

        List<Doctor> doctors = new ArrayList<>();

        for(JsonElement elem : arr){
            JsonObject obj = elem.getAsJsonObject();

            int userId = obj.get("id").getAsInt();
            String email = obj.get("email").getAsString();
            String firstName = obj.get("first_name").getAsString();
            String lastName = obj.get("last_name").getAsString();
            String password = obj.get("password").getAsString();

            Doctor doctor = new Doctor(userId, email, firstName, lastName, password, specialization);
            doctors.add(doctor);
        }

        return doctors;
    }


    public static int saveUser(User user, String role) throws Exception {
        JsonObject json = JsonTransformer.userToJson(user, role);

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

        JsonObject json = JsonTransformer.userToJson(user);

        String result = SupabaseClient.patch("User", user.getId(), json.toString());

        // Return the id of the updated user (should be same as before)
        JsonArray arr = gson.fromJson(result, JsonArray.class);
        if (!arr.isEmpty()) {
            return arr.get(0).getAsJsonObject().get("id").getAsInt();
        } else {
            throw new Exception("Failed to update user, no response returned.");
        }
    }

    public static List<Integer> fetchDoctorIdsBySpecialization(String specialization) throws Exception {
        // Only doctors with given specialization, only their ids
        String endpoint = "User?role=eq.doctor&specialization=eq." + specialization + "&select=id";

        String json = SupabaseClient.get(endpoint);
        JsonArray arr = JsonParser.parseString(json).getAsJsonArray();

        List<Integer> ids = new ArrayList<>();
        for (JsonElement el : arr) {
            JsonObject obj = el.getAsJsonObject();
            ids.add(obj.get("id").getAsInt());
        }
        return ids;
    }

    public static List<Integer> fetchUserIdsByName(String fullName) throws Exception {
        if (fullName == null || fullName.trim().isEmpty()) return java.util.Collections.emptyList();

        String trimmed = fullName.trim();
        String[] parts = trimmed.split("\\s+", 2);
        String first = parts[0];
        String last = parts.length > 1 ? parts[1] : "";

        String endpoint;
        if (!last.isEmpty()) {
            endpoint = "User?first_name=eq." + first + "&last_name=eq." + last + "&select=id";
        } else {
            // If only one token provided, search by first_name
            endpoint = "User?first_name=eq." + first + "&select=id";
        }

        String json = SupabaseClient.get(endpoint);
        com.google.gson.JsonArray arr = com.google.gson.JsonParser.parseString(json).getAsJsonArray();
        List<Integer> ids = new ArrayList<>();
        for (com.google.gson.JsonElement el : arr) {
            com.google.gson.JsonObject obj = el.getAsJsonObject();
            ids.add(obj.get("id").getAsInt());
        }
        return ids;
    }

    public static void deleteUser(int userId) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be valid to delete");
        }
        SupabaseClient.delete("User", userId, "");   // or delete("User", userId, "")
    }
}
