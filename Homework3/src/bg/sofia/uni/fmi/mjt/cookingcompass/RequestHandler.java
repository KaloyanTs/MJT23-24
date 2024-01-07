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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestHandler {

    private static final Gson gson;
    private static Set<String> dishTypes;
    private static Set<String> healthTypes;

    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
        dishTypes = new HashSet<>();
        dishTypes.add("alcohol%20cocktail");
        dishTypes.add("biscuits%20and%20cookies");
        dishTypes.add("bread");
        dishTypes.add("cereals");
        dishTypes.add("condiments and sauces");
        dishTypes.add("desserts");
        dishTypes.add("drinks");
        dishTypes.add("egg");
        dishTypes.add("ice cream and custard");
        dishTypes.add("main course");
        dishTypes.add("pancake");
        dishTypes.add("pasta");
        dishTypes.add("pastry");
        dishTypes.add("pies and tarts");
        dishTypes.add("pizza");
        dishTypes.add("preps");
        dishTypes.add("preserve");
        dishTypes.add("salad");
        dishTypes.add("sandwiches");
        dishTypes.add("seafood");
        dishTypes.add("side dish");
        dishTypes.add("soup");
        dishTypes.add("special occasions");
        dishTypes.add("starter");
        dishTypes.add("sweets");
        healthTypes = new HashSet<>();
        healthTypes.add("alcohol-cocktail");
        healthTypes.add("alcohol-free");
        healthTypes.add("celery-free");
        healthTypes.add("cean-free");
        healthTypes.add("dairy-free");
        healthTypes.add("DASH");
        healthTypes.add("egg-free");
        healthTypes.add("fish-free");
        healthTypes.add("fodmap-free");
        healthTypes.add("gluten-free");
        healthTypes.add("immuno-supportive");
        healthTypes.add("keto-friendly");
        healthTypes.add("kidney-friendly");
        healthTypes.add("kosher");
        healthTypes.add("low-potassium");
        healthTypes.add("low-sugar");
        healthTypes.add("lupine-free");
        healthTypes.add("Mediterranean");
        healthTypes.add("mollusk-free");
        healthTypes.add("mustard-free");
        healthTypes.add("No-oil-added");
        healthTypes.add("paleo");
        healthTypes.add("peanut-free");
        healthTypes.add("pecatarian");
        healthTypes.add("pork-free");
        healthTypes.add("red-meat-free");
        healthTypes.add("sesame-free");
        healthTypes.add("shellfish-free");
        healthTypes.add("soy-free");
        healthTypes.add("sugar-conscious");
        healthTypes.add("sulfite-free");
        healthTypes.add("tree-nut-free");
        healthTypes.add("vegan");
        healthTypes.add("vegetarian");
        healthTypes.add("wheat-free");
    }

    public static List<Recipe> byKeywords(List<String> keywords) {
        try (HttpClient client = HttpClient.newBuilder().build()) {
            String type = "type=public&";

            StringBuilder q = new StringBuilder("");

            for (String word :
                keywords.stream().filter(keyword -> !healthTypes.contains(keyword) && !dishTypes.contains(keyword))
                    .toList()) {
                q.append("q=").append(word).append("&");
            }
            String app_id_key = "app_id=4abc3395&app_key=b5cd0fc2f0bd828ebc6f6796cd5fe5e1&";

            StringBuilder health = new StringBuilder("");

            for (String word : keywords.stream().filter(keyword -> healthTypes.contains(keyword)).toList()) {
                health.append("healthLabels=").append(word).append("&");
            }

            StringBuilder dishType = new StringBuilder("");

            for (String word : keywords.stream().filter(keyword -> dishTypes.contains(keyword)).toList()) {
                dishType.append("dishType=").append(word).append("&");
            }

            String field = "field=label" + "&"
                + "field=totalWeight" + "&"
                + "field=dietLabels" + "&"
                + "field=healthLabels" + "&"
                + "field=cuisineType" + "&"
                + "field=mealType" + "&"
                + "field=dishType" + "&"
                + "field=ingredientLines";

            URL uri = new URI("https://api.edamam.com/api/recipes/v2" + "?" + type
                + q
                + app_id_key
                + health
                + dishType
                + field).toURL();

            List<Recipe> result = new ArrayList<>();

            do {
                HttpRequest request = HttpRequest.newBuilder().uri(uri.toURI()).build();
                HttpResponse<String> responseJson = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (responseJson.statusCode() != 200) {
                    throw new IllegalStateException(responseJson.body());
                }

                JsonElement resultJson = JsonParser.parseString(responseJson.body());

                result.addAll(resultJson
                    .getAsJsonObject().get("hits")
                    .getAsJsonArray().asList().stream()
                    .map(x -> x.getAsJsonObject().get("recipe").getAsJsonObject())
                    .toList().stream()
                    .map(x -> gson.fromJson(x, Recipe.class)).toList());


                JsonElement links = resultJson.getAsJsonObject().get("_links");
                if (links.getAsJsonObject().isEmpty()) {
                    return result;
                }


                String nextPage =
                    gson.fromJson(
                        links.getAsJsonObject().get("next").getAsJsonObject().get(
                            "href"), String.class);

                System.out.println(nextPage);

                uri = new URL(nextPage);
            }
            while (true);

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new IllegalStateException("Something unexpected happened...", e);
        }

    }
}
