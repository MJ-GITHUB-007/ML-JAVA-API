package deploy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static String readApiKeyFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {

        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));

        // Create an instance of MLApiClient
        MLApiClient mlApiClient = new MLApiClient();

        // Read the API key from the file
        String apiKey = readApiKeyFromFile("./deploy/my_key.txt");

        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("API key not found. Please check the api-key file.");
            return;
        }

        try {
            // Read the input
            System.out.print("\nEnter feature1, feature2, feature3, feature4: ");
            String inputLine = bReader.readLine();

            // Split the input line by commas and trim each value
            String[] inputValues = inputLine.split(",");
            float feature1 = Float.parseFloat(inputValues[0].trim());
            float feature2 = Float.parseFloat(inputValues[1].trim());
            float feature3 = Float.parseFloat(inputValues[2].trim());
            float feature4 = Float.parseFloat(inputValues[3].trim());

            String predictionResponse = mlApiClient.predict(feature1, feature2, feature3, feature4, apiKey);

            // Print the prediction response
            System.out.println("Predicted class: " + predictionResponse + "\n");
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid input. Please enter four valid numeric values separated by commas.");
        }
    }
}

