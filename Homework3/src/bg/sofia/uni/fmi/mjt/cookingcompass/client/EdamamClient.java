package bg.sofia.uni.fmi.mjt.cookingcompass.client;

import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.EdamamPageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.handler.EdamamRequestHandler;
import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.PageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.apiagent.WebAPIAgent;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EdamamClient extends APIClient {

    private String appId;
    private String appKey;
    private static Set<String> healthLabels;
    private static Set<String> dishType;

    static {
        healthLabels =
            Set.of("alcohol-cocktail", "alcohol-free", "celery-free", "cean-free", "dairy-free", "DASH", "egg-free",
                "fish-free", "fodmap-free", "gluten-free", "immuno-supportive", "keto-friendly", "kidney-friendly",
                "kosher", "low-potassium", "low-sugar", "lupine-free", "Mediterranean", "mollusk-free", "mustard-free",
                "No-oil-added", "paleo", "peanut-free", "pecatarian", "pork-free", "red-meat-free", "sesame-free",
                "shellfish-free", "soy-free", "sugar-conscious", "sulfite-free", "tree-nut-free", "vegan", "vegetarian",
                "wheat-free");

        dishType =
            Set.of("alcohol%20cocktail", "biscuits%20and%20cookies", "bread", "cereals", "condiments%20and%20sauces",
                "desserts", "drinks", "egg", "ice%20cream%20and%20custard", "main%20course", "pancake", "pasta",
                "pastry", "pies%20and%20tarts", "pizza", "preps", "preserve", "salad", "sandwiches", "seafood",
                "side%20dish", "soup", "special%20occasions", "starter", "sweets");
    }

    @Override
    public APIClient user(String... credentials) {
        if (credentials.length != 2) {
            throw new IllegalArgumentException("Edamam Authorization needs Id and Key");
        }
        appId = credentials[0];
        appKey = credentials[1];
        hasCredentials = true;
        return this;
    }

    public EdamamClient(EdamamRequestHandler handler) {
        super(handler);
    }

    public EdamamClient(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
        Map<String, Set<String>> keywordsGrouped = new HashMap<>();
        keywordsGrouped.put("healthLabels", healthLabels);
        keywordsGrouped.put("dishType", dishType);

        WebAPIAgent agent = new WebAPIAgent("https://api.edamam.com/api/recipes/v2", appId,
            appKey, keywordsGrouped);

        PageMover pageMover = new EdamamPageMover(new GsonBuilder().setPrettyPrinting().create());

        handler = new EdamamRequestHandler(agent, pageMover);
    }
}
