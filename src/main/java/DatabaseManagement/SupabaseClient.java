package DatabaseManagement;
import java.net.URI;
import java.net.http.*;

public class SupabaseClient {

    private static final String URL = "https://YOUR_PROJECT_ID.supabase.co/rest/v1/";
    private static final String API_KEY = "YOUR_SERVICE_ROLE_KEY";

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
}

