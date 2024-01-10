package bg.sofia.uni.fmi.mjt.cookingcompass.apiagent;

import bg.sofia.uni.fmi.mjt.cookingcompass.RequestHandler;
import bg.sofia.uni.fmi.mjt.cookingcompass.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.HttpRequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.response.RequestResponse;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class WebRequestHandler extends RequestHandler {
    //todo gets keywords and returns recipes from the web

    private static final int WEB_OK_CODE = 200;
    private final HttpRequestCreator requestCreator;

    public WebRequestHandler(WebAPIAgent agent) {
        this.requestCreator = new HttpRequestCreator(agent);
    }

    public RequestResponse getByKeywords(String... keywords) {
        List<Recipe> recipeList = new ArrayList<>();
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
                nextPage = getNextPage(element);

            } while (!nextPage.isEmpty());

        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new IllegalStateException("Something unexpected occurred...", e);
        }
        return new RequestResponse(httpResponse.statusCode(), GSON.toJson(recipeList));
    }

    private static String getNextPage(JsonElement element) {
        String nextPage;
        JsonElement links = element.getAsJsonObject().get("_links");
        if (links.getAsJsonObject().isEmpty()) {
            nextPage = "";
        } else {
            nextPage =
                GSON.fromJson(
                    links.getAsJsonObject().get("next").getAsJsonObject().get(
                        "href"), String.class);
        }
        return nextPage;
    }

    private List<Recipe> convert(JsonElement element) {
        return element.getAsJsonObject().get("hits")
            .getAsJsonArray().asList().stream()
            .map(x -> GSON.fromJson(x.getAsJsonObject().get("recipe")
                .getAsJsonObject(), Recipe.class))
            .toList();
    }
}
