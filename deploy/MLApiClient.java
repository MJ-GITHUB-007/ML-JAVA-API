package ml_dep;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class MLApiClient {

    private static String extractPredictedClass(String response) {
        int startIndex = response.indexOf(":") + 2; // Index of the beginning of the class name
        int endIndex = response.indexOf("\"", startIndex); // Index of the end of the class name
        return response.substring(startIndex, endIndex);
    }

    public String predict(float feature1, float feature2, float feature3, float feature4, String apiKey) {
        // Set up HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Set up HTTP request with query parameters and API key in the header
        HttpRequest request = HttpRequest.newBuilder()
                .uri(
                    URI.create(String.format("http://127.0.0.1:8000/predict?feature1=%f&feature2=%f&feature3=%f&feature4=%f",
                    feature1, feature2, feature3, feature4))
                )
                .header("Content-Type", "application/json")
                .header("API-Key", apiKey)  // Include the API key in the header
                .GET()
                .build();

        // Send HTTP request and get response asynchronously
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        // Block and get the response body
        try {
            String responseBody = response.thenApply(HttpResponse::body).join();
            return extractPredictedClass(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
