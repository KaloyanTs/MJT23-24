package bg.sofia.uni.fmi.mjt.cookingcompass.apiagent;

import bg.sofia.uni.fmi.mjt.cookingcompass.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.HttpRequestCreator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class WebRequestHandler<T> {
    //todo gets keywords and returns recipes from the web

    private static final Gson gson;
    private final HttpRequestCreator requestCreator;

    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public WebRequestHandler(WebAPIAgent agent) {
        this.requestCreator = new HttpRequestCreator(agent);
    }

    public List<Recipe> getByKeywords(String... keywords) {
        List<Recipe> recipeList = new ArrayList<>();
        HttpResponse<String> httpResponse;
        String nextPage = "";
        HttpRequest request;
        try (HttpClient client = HttpClient.newBuilder().build()) {

            do {
                if (!nextPage.equals("")) {
                    request = requestCreator.makeRequest(nextPage);
                } else {
                    request = requestCreator.makeRequest(keywords);
                }

                httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
                JsonElement element = gson.fromJson(httpResponse.body(), JsonElement.class);
                recipeList.addAll(convert(element));
                nextPage = getNextPage(element);

            } while (!nextPage.equals(""));

        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new IllegalStateException("Something unexpected occurred...", e);
        }

        return recipeList;
    }

    private static String getNextPage(JsonElement element) {
        String nextPage;
        JsonElement links = element.getAsJsonObject().get("_links");
        if (links.getAsJsonObject().isEmpty()) {
            nextPage = "";
        } else {
            nextPage =
                gson.fromJson(
                    links.getAsJsonObject().get("next").getAsJsonObject().get(
                        "href"), String.class);
        }
        return nextPage;
    }

    private static List<Recipe> convert(JsonElement element) {

        return element.getAsJsonObject().get("hits")
            .getAsJsonArray().asList().stream()
            .map(x -> gson.fromJson(x.getAsJsonObject().get("recipe")
                .getAsJsonObject(), Recipe.class))
            .toList();
    }
}
