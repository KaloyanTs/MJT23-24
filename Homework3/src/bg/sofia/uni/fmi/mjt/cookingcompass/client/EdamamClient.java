package bg.sofia.uni.fmi.mjt.cookingcompass.client;

import bg.sofia.uni.fmi.mjt.cookingcompass.api.WebAPIRepresentative;
import bg.sofia.uni.fmi.mjt.cookingcompass.page.EdamamPageMover;
import bg.sofia.uni.fmi.mjt.cookingcompass.exception.BadCredentialsException;
import bg.sofia.uni.fmi.mjt.cookingcompass.request.EdamamRequestCreator;
import bg.sofia.uni.fmi.mjt.cookingcompass.retriever.EdamamDataRetriever;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EdamamClient extends APIClient {

    private String appId;
    private String appKey;
    private static final Map<String, Set<String>> KEYWORDS_GROUPED;

    static {
        KEYWORDS_GROUPED = new HashMap<>();
        KEYWORDS_GROUPED.put("healthLabels",
            Set.of("alcohol-cocktail", "alcohol-free", "celery-free", "cean-free", "dairy-free", "DASH", "egg-free",
                "fish-free", "fodmap-free", "gluten-free", "immuno-supportive", "keto-friendly", "kidney-friendly",
                "kosher", "low-potassium", "low-sugar", "lupine-free", "Mediterranean", "mollusk-free", "mustard-free",
                "No-oil-added", "paleo", "peanut-free", "pecatarian", "pork-free", "red-meat-free", "sesame-free",
                "shellfish-free", "soy-free", "sugar-conscious", "sulfite-free", "tree-nut-free", "vegan", "vegetarian",
                "wheat-free"));
        KEYWORDS_GROUPED.put("dishType",
            Set.of("alcohol%20cocktail", "biscuits%20and%20cookies", "bread", "cereals", "condiments%20and%20sauces",
                "desserts", "drinks", "egg", "ice%20cream%20and%20custard", "main%20course", "pancake", "pasta",
                "pastry", "pies%20and%20tarts", "pizza", "preps", "preserve", "salad", "sandwiches", "seafood",
                "side%20dish", "soup", "special%20occasions", "starter", "sweets"));

    }

    @Override
    public void setCredentials(String... credentials) throws BadCredentialsException {
        if (credentials.length != 2) {
            throw new BadCredentialsException("Edamam Authorization needs Id and Key");
        }
        appId = credentials[0];
        appKey = credentials[1];
    }

    public EdamamClient(String appId, String appKey) {
        super(
            new EdamamDataRetriever(
                new EdamamPageMover(
                    new Gson()),
                new EdamamRequestCreator(
                    new WebAPIRepresentative(
                        "https://api.edamam.com/api/recipes/v2",
                        appId,
                        appKey,
                        KEYWORDS_GROUPED))
            )
        );
        try {
            user(appId, appKey);
        } catch (BadCredentialsException e) {
            throw new IllegalStateException("Something uncexpected occured...", e);
        }
    }

    public String getAppId() {
        return appId;
    }

    public String getAppKey() {
        return appKey;
    }
}
