package bg.sofia.uni.fmi.mjt.passvault.password;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PasswordChecker {

    public PasswordChecker() {
        //todo implement
        //todo make interface
    }

    boolean checkPassword(Password password) {
        HttpRequest httpRequest;
        HttpResponse<String> httpResponse;
        try {
            httpRequest = HttpRequest.newBuilder().uri(new URI(request.getData())).build();
            httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Something unexpected occurred...", e);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Given request could not be converted to URI...", e);
        }
        return new RawResponse(httpResponse.statusCode(), httpResponse.body());

    }

    public static void main(String[] args) {

    }
}
