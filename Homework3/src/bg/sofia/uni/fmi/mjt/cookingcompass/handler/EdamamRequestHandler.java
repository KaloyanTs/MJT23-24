package bg.sofia.uni.fmi.mjt.cookingcompass.handler;

import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.PageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.WebAPIAgent;
import bg.sofia.uni.fmi.mjt.cookingcompass.unit.EdamamRecipe;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.EdamamRequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class EdamamRequestHandler extends RequestHandler {
    //todo gets keywords and returns recipes from the web

    private static final int WEB_OK_CODE = 200;
    private final EdamamRequestCreator requestCreator;
    private final PageMover pageMover;

    public EdamamRequestHandler(WebAPIAgent agent, PageMover pageMover) {
        this.requestCreator = new EdamamRequestCreator(agent);
        this.pageMover = pageMover;
    }

    public RequestResponse getByKeywords(String... keywords) {
        List<EdamamRecipe> recipeList = new ArrayList<>();
        HttpResponse<String> httpResponse;
        String nextPage = "";
        HttpRequest request;
        try (HttpClient client = HttpClient.newBuilder().build()) {

            do {
                if (!nextPage.isEmpty()) {
                    request = requestCreator.makeRequest(nextPage);
                } else {
                    request = requestCreator.makeRequest(keywords);
                }

                httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (httpResponse.statusCode() != WEB_OK_CODE) {
                    return new RequestResponse(httpResponse.statusCode(), httpResponse.body());
                }

                JsonElement element = GSON.fromJson(httpResponse.body(), JsonElement.class);
                recipeList.addAll(convert(element));
                nextPage = pageMover.getNextPage(element);

            } while (!nextPage.isEmpty());

        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new IllegalStateException("Something unexpected occurred...", e);
        }
        return new RequestResponse(httpResponse.statusCode(), GSON.toJson(recipeList));
    }

    private List<EdamamRecipe> convert(JsonElement element) {
        return element.getAsJsonObject().get("hits")
            .getAsJsonArray().asList().stream()
            .map(x -> GSON.fromJson(x.getAsJsonObject().get("recipe")
                .getAsJsonObject(), EdamamRecipe.class))
            .toList();
    }
}
