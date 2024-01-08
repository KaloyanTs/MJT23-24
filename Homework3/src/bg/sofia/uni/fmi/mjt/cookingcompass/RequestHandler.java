package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.recipe.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestHandler {

    private static final Gson gson;
    private static final Set<String> dishTypes;
    private static final Set<String> healthTypes;
    private final RecipeRequestResponder requestResponder;

    static {
        gson = new GsonBuilder().setPrettyPrinting().create();
        dishTypes = new HashSet<>();
        dishTypes.add("alcohol%20cocktail");
        dishTypes.add("biscuits%20and%20cookies");
        dishTypes.add("bread");
        dishTypes.add("cereals");
        dishTypes.add("condiments%20and%20sauces");
        dishTypes.add("desserts");
        dishTypes.add("drinks");
        dishTypes.add("egg");
        dishTypes.add("ice%20cream%20and%20custard");
        dishTypes.add("main%20course");
        dishTypes.add("pancake");
        dishTypes.add("pasta");
        dishTypes.add("pastry");
        dishTypes.add("pies%20and%20tarts");
        dishTypes.add("pizza");
        dishTypes.add("preps");
        dishTypes.add("preserve");
        dishTypes.add("salad");
        dishTypes.add("sandwiches");
        dishTypes.add("seafood");
        dishTypes.add("side%20dish");
        dishTypes.add("soup");
        dishTypes.add("special%20occasions");
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

    public RequestHandler(RecipeRequestResponder requestResponder) {
        this.requestResponder = requestResponder;
    }

    public List<Recipe> byKeywords(List<String> keywords) {
        String type = "type=public&";

        StringBuilder q = new StringBuilder();

        for (String word :
            keywords.stream().filter(keyword -> !healthTypes.contains(keyword) && !dishTypes.contains(keyword))
                .toList()) {
            q.append("q=").append(word).append("&");
        }
        String app_id_key = "app_id=4abc3395&app_key=b5cd0fc2f0bd828ebc6f6796cd5fe5e1&";

        StringBuilder health = new StringBuilder();

        for (String word : keywords.stream().filter(healthTypes::contains).toList()) {
            health.append("healthLabels=").append(word).append("&");
        }

        StringBuilder dishType = new StringBuilder();

        for (String word : keywords.stream().filter(dishTypes::contains).toList()) {
            dishType.append("dishType=").append(word).append("&");
        }

        String request = "https://api.edamam.com/api/recipes/v2" + "?" + type
            + q
            + app_id_key
            + health
            + dishType
            + "field=label" + "&"
            + "field=totalWeight" + "&"
            + "field=dietLabels" + "&"
            + "field=healthLabels" + "&"
            + "field=cuisineType" + "&"
            + "field=mealType" + "&"
            + "field=dishType" + "&"
            + "field=ingredientLines";

        List<Recipe> result = new ArrayList<>();

        RecipeRequestResponse requestResponse;

        do {
            try {
                requestResponse = requestResponder.proceedRequest(request);
            } catch (URISyntaxException | MalformedURLException e) {
                throw new IllegalStateException("Something unexpected occurred...", e);
            }

            if (requestResponse.statusCode() != 200) {
                throw new IllegalStateException(requestResponse.body());
            }

            Type typeRecipe = new TypeToken<List<Recipe>>() {
            }.getType();
            List<Recipe> list = gson.fromJson(requestResponse.body(), typeRecipe);
            result.addAll(list);

            if (requestResponse.nextPage().isEmpty()) {
                return result;
            }

            request = requestResponse.nextPage();
        }
        while (true);

    }
}
