package bg.sofia.uni.fmi.mjt.cookingcompass.retriever;

import bg.sofia.uni.fmi.mjt.cookingcompass.page.EdamamPageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.EdamamRequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.Request;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RawResponse;
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
import java.util.List;

public class EdamamDataRetriever extends PagedDataRetriever {

    private static final int OK_STATUS = 200;
    private final HttpClient client;
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }

    public EdamamDataRetriever(EdamamPageMover edamamPageMover, EdamamRequestCreator requestCreator) {
        super(OK_STATUS,
            edamamPageMover,
            requestCreator);
        client = HttpClient
            .newBuilder()
            .build();
    }

    @Override
    protected RawResponse retrieveData(Request request) {
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

    @Override
    protected RequestResponse convertResponse(RawResponse element) {
        return new RequestResponse(element.statusCode() == OK_STATUS, element.statusCode(),
            (element.statusCode() == OK_STATUS ? GSON.fromJson(element.bodyJson(), JsonElement.class).getAsJsonObject()
                .get("hits").getAsJsonArray().asList().stream().map(x -> x.getAsJsonObject().get("recipe")).toList() :
                List.of()));
    }
}
