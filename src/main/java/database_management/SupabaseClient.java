package database_management;
import java.net.URI;
import java.net.http.*;

public class SupabaseClient {

    private static final String URL = "https://zwfjbeiquyscxgcssgby.supabase.co/rest/v1/";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp3ZmpiZWlxdXlzY3hnY3NzZ2J5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQyNjQ0NTgsImV4cCI6MjA3OTg0MDQ1OH0.MFHgLj3bTD4hCNlHD0jB5b5MKm3psnh_sn5V1CxLjqI";

    private static final HttpClient client = HttpClient.newHttpClient();

    public static String get(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + endpoint))
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String post(String endpoint, String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + endpoint))
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
    public static String patch(String endpoint, int id, String json) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL + endpoint + "?id=eq." + id)) // filter by id
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}

