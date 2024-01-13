package bg.sofia.uni.fmi.mjt.cookingcompass.retriever;

import bg.sofia.uni.fmi.mjt.cookingcompass.page.EdamamPageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.EdamamRequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.Request;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawRequestResponse;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EdamamDataRetriever extends PagedDataRetriever {

    private HttpClient client;
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }

    public EdamamDataRetriever(EdamamPageMover edamamPageMover, EdamamRequestCreator requestCreator) {
        super(edamamPageMover, requestCreator);
        client = HttpClient.newBuilder().build();
    }

    @Override
    protected RawRequestResponse retrieveData(Request request) {
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
        return new RawRequestResponse(httpResponse.statusCode(), httpResponse.body());
    }

    @Override
    protected RequestResponse convertResponse(RawRequestResponse element) {
        return new RequestResponse(element.statusCode(),
            GSON.fromJson(element.bodyJson(), JsonElement.class)
                .getAsJsonObject().get("hits")
                .getAsJsonArray().asList().stream()
                .map(x -> x.getAsJsonObject().get("recipe")).toList());
    }
}
