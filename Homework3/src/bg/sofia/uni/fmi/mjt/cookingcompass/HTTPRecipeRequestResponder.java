package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.recipe.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPRecipeRequestResponder implements RecipeRequestResponder {

    private static final Gson gson;

    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public RecipeRequestResponse proceedRequest(String request) {
        HttpResponse<String> httpResponse;
        String jsonRecipes;
        String nextPage;
        try (HttpClient client = HttpClient.newBuilder().build()) {
            URL uri = new URI(request).toURL();

            HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri.toURI()).build();
            httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            JsonElement resultJson = JsonParser.parseString(httpResponse.body());

            jsonRecipes = gson.toJson(
                resultJson.getAsJsonObject().get("hits")
                    .getAsJsonArray().asList().stream()
                    .map(x -> gson.fromJson(x.getAsJsonObject().get("recipe")
                        .getAsJsonObject(), Recipe.class))
                    .toList());

            JsonElement links = resultJson.getAsJsonObject().get("_links");
            if (links.getAsJsonObject().isEmpty()) {
                nextPage = "";
            } else {
                nextPage =
                    gson.fromJson(
                        links.getAsJsonObject().get("next").getAsJsonObject().get(
                            "href"), String.class);
            }


        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return new RecipeRequestResponse(httpResponse.statusCode(),
            jsonRecipes,
            nextPage);
    }
}
