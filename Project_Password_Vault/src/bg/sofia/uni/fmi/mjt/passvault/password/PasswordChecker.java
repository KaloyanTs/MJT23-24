package bg.sofia.uni.fmi.mjt.passvault.password;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PasswordChecker {

    //private static final String apiKey = "YOUR_KEY_HERE";
    //private static final String apiSecret = "YOUR_SECRET_HERE";
    //todo uncomment before submit
    private static final String API_KEY = "de4f91a94a7d42a3807c616eccdfd42c";
    private static final String API_SECRET = "wvP=s14&YzF1_b#@YQ3MTXn^Vz3=bs*5";

    public PasswordChecker() {
        //todo implement
        //todo make interface
    }

    protected HttpRequest makeRequest(Password password) throws URISyntaxException {
        String credentials = API_KEY + ":" + API_SECRET;
        String encodedCredentials = Base64
            .getEncoder()
            .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        String str = "{\"partialSHA256\": \"" + password.getCiphered("SHA256").substring(0, 9) + "\"," +
            "\"partialSHA1\": \"" + password.getCiphered("SHA1").substring(0, 9) + "\"," +
            "\"partialMD5\": \"" + password.getCiphered("MD5").substring(0, 9) + "\"" +
            "}";
        return HttpRequest.newBuilder()
            .uri(URI.create("https://api.enzoic.com/passwords?"))
            .header("Authorization", "Basic " + encodedCredentials)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(
                "{\"partialSHA256\": \"" + password.getCiphered("SHA256").substring(0, 9) + "\"," +
                    "\"partialSHA1\": \"" + password.getCiphered("SHA1").substring(0, 9) + "\"," +
                    "\"partialMD5\": \"" + password.getCiphered("MD5").substring(0, 9) + "\"" +
                    "}"))
            .build();
    }

    public boolean checkPassword(Password password) {
        HttpClient client = HttpClient
            .newBuilder()
            .build();
        HttpRequest httpRequest;
        HttpResponse<String> httpResponse;
        try {
            httpRequest = makeRequest(password);
            httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Something unexpected occurred...", e);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Given request could not be converted to URI...", e);
        }
        System.out.println(httpResponse.body());
        return true;
    }

    public static void main(String[] args) {
        PasswordChecker passwordChecker = new PasswordChecker();
        passwordChecker.checkPassword(Password.of("apple"));
    }
}